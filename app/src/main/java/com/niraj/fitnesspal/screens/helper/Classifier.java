package com.niraj.fitnesspal.screens.helper;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.database.annotations.Nullable;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Interpreter.Options;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class Classifier {
    private final int INPUT_SIZE;
    private final int PIXEL_SIZE = 3;
    private final int IMAGE_MEAN = 0;
    private final float IMAGE_STD = 255.0F;
    private final int MAX_RESULTS = 3;
    private final float THRESHOLD = 0.55F;
    private final int DEFAULT_BUFFER_SIZE = 8 * 1024;
    private Interpreter interpreter;
    private List<FoodItem> labelList;

    public Classifier(@NotNull AssetManager assetManager, @NotNull String modelPath, @NotNull String labelPath, int inputSize) throws IOException {
        super();
        this.INPUT_SIZE = inputSize;
        Options options = new Options();
        options.setNumThreads(5);
        options.setUseNNAPI(true);
        this.interpreter = new Interpreter(this.loadModelFile(assetManager, modelPath), options);
        this.labelList = this.loadLabelList(assetManager, labelPath);
    }


    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private List<FoodItem> loadLabelList(AssetManager assetManager, String labelPath) throws IOException {
        InputStream inputStream = assetManager.open(labelPath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8), DEFAULT_BUFFER_SIZE);
        String strLine;

        List<FoodItem> output = new ArrayList<>();

        while ((strLine = reader.readLine()) != null) {
            String[] columns = strLine.split(",");
            String title = columns[0].trim();
            String itemUnit = columns.length > 1 ? columns[1].trim() : "Per Piece";
            float calories = columns.length > 2 ? Float.parseFloat(columns[2].trim()) : 0f;
            float carb = columns.length > 3 ? Float.parseFloat(columns[3].trim()) : 0f;
            float protein = columns.length > 4 ? Float.parseFloat(columns[4].trim()) : 0f;
            float fat = columns.length > 5 ? Float.parseFloat(columns[5].trim()) : 0f;
            output.add(new FoodItem(title, itemUnit, calories, carb, protein, fat));
        }

        // Close the input stream
        inputStream.close();
        return output;
    }

    /**
     * Returns the result after running the recognition with the help of interpreter
     * on the passed bitmap
     */
    @NotNull
    public final List<Classifier.Recognition> recognizeImage(@NotNull Bitmap bitmap) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, this.INPUT_SIZE, this.INPUT_SIZE, false);
        ByteBuffer byteBuffer = this.convertBitmapToByteBuffer(scaledBitmap);
        float[][] result = new float[1][];

        for (int i = 0; i < 1; ++i) {
            float[] value = new float[this.labelList.size()];
            result[i] = value;
        }

        this.interpreter.run(byteBuffer, result);
        return getSortedResult(result);
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * this.INPUT_SIZE * this.INPUT_SIZE * this.PIXEL_SIZE);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[this.INPUT_SIZE * this.INPUT_SIZE];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        int inputSize = 0;

        for (; inputSize < this.INPUT_SIZE; ++inputSize) {
            int inputSizeInternal = 0;

            for (; inputSizeInternal < this.INPUT_SIZE; ++inputSizeInternal) {
                int input = intValues[pixel++];
                byteBuffer.putFloat((float) ((input >> 16 & 255) - this.IMAGE_MEAN) / this.IMAGE_STD);
                byteBuffer.putFloat((float) ((input >> 8 & 255) - this.IMAGE_MEAN) / this.IMAGE_STD);
                byteBuffer.putFloat((float) ((input & 255) - this.IMAGE_MEAN) / this.IMAGE_STD);
            }
        }

        return byteBuffer;
    }

    private List<Classifier.Recognition> getSortedResult(float[][] labelProbArray) {
        PriorityQueue<Recognition> pq = new PriorityQueue<>(MAX_RESULTS, (o1, o2) -> Float.compare(o1.confidence, o2.confidence) * -1);
        int i = 0;
        int recognitionsSize;
        for (recognitionsSize = ((Collection) this.labelList).size(); i < recognitionsSize; ++i) {
            float confidence = labelProbArray[0][i];
            if (confidence >= this.THRESHOLD) {
                FoodItem foodItem = labelList.size() > i ? this.labelList.get(i) : null;
                pq.add(new Classifier.Recognition("" + i, foodItem, confidence));
            }
        }

        ArrayList<Classifier.Recognition> recognitions = new ArrayList<>();
        recognitionsSize = Math.min(pq.size(), this.MAX_RESULTS);
        int var16 = 0;

        for (int var6 = recognitionsSize; var16 < var6; ++var16) {
            recognitions.add(pq.poll());
        }

        return recognitions;
    }

    public static final class Recognition {
        @NotNull
        private final String id;
        @Nullable
        private final FoodItem foodItem;
        @NotNull
        private final float confidence;

        public Recognition(@NotNull String id, @Nullable FoodItem foodItem, @NotNull float confidence) {
            this.id = id;
            this.foodItem = foodItem;
            this.confidence = confidence;
        }

        public FoodItem getFoodItem() {
            return foodItem;
        }
    }

    public static final class FoodItem {
        @NotNull
        private final String title;
        @NotNull
        private final float calories;
        @NotNull
        private final float carb;
        @NotNull
        private final float protein;
        @NotNull
        private final float fat;
        @NotNull
        private String itemUnit;

        public FoodItem(@NotNull String title, @NotNull String itemUnit, @NotNull float calories, float carb, float protein, float fat) {
            this.title = title;
            this.itemUnit = itemUnit;
            this.calories = calories;
            this.carb = carb;
            this.protein = protein;
            this.fat = fat;
        }

        public String getTitle() {
            return title;
        }

        public String getItemUnit() {
            return itemUnit;
        }

        public float getCalories() {
            return calories;
        }

        public float getCarb() {
            return carb;
        }

        public float getFat() {
            return fat;
        }

        public float getProtein() {
            return protein;
        }
    }
}
