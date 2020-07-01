# Fitness-Pal-App
## About the Installation of the FintessPal App
#### *** 1 Register and Sign Up:***
Fill in the Information that is required in Register and Sign Up.
#### *** 2 Login:***
After Filling up the register form and signing Up, Login with the same credentials.
#### *** 3 Tracking a Food:***
After Logging in, you'll be given an option to either take a photo of the food or choose from gallery to track the food.
#### *** 4 Adding a Tracked Food into Journal:***
After tracking a food, you'll be given an option to add the tracked food into journal with quantity respectively.
#### *** 5 Checking the food Journal:***
After adding foods to the journal, an option in the top right corner of the screen will show the tracked and added foods with its calorie and the day's total calorie respectively.
#### ***6 Logout Button***.
You can logout using the Logout button which will save all your calories in your account only.

---
### List of Foods this project supports:
chicken curry	    fried rice	     samosa
chicken wings	    garlic bread     sandwich
chocolate cake	    hamburger        soup
cup cake	    hot-dog	     spring rolls
donuts	            ice-cream	     sushi
dumplings	    omelette         waffles
french fries	    pizza


---
### To avoid overfitting
from keras.preprocessing.image import ImageDataGeneratortrain_datagen = ImageDataGenerator(rescale = 1./255, rotation_range=360,width_shift_range=0.2,height_shift_range=0.2, shear_range = 0.2,zoom_range = [0.5, 1.0],brightness_range = [0.2, 1.0],horizontal_flip = True,vertical_flip =False,zca_whitening=True, zca_epsilon=1e-06)training_set=train_datagen.flow_from_directory(path_training,target_size =(256,256),batch_size=64,class_mode='categorical',shuffle=True)

---
### Testing
imn = '/content/test/testimage.jpg' #test image path
img = load_img(imn, target_size=(256, 256))
img = np.asarray(img)
img = img.astype('float32')
img = img/255
img = np.expand_dims(img, axis=0)
img = img.reshape(1,256,256,3)
res = model2.predict(img)
ord = np.argsort(res)
ind = np.argmax(res)li = ['chicken curry', 'chicken wings', 'ch cake', 'cup cake', 'donuts', 'dumplings', 'fries','fried rice', 'garlic bread', 'hamburger', 'hot-dog', 'ice-cream', 'omelette', 'pizza', 'samosa', 'sandwich','soup','spring rolls', 'sushi', 'waffle']lis = []for i in range(0, 5):
  lis.append(li[(ord[0][19 - i])])print(lis) # Top-5 predictions


---
### Conversion to tensorflow Lite

image_shape = (256, 256, 3)def representative_dataset_gen():
  num_calibration_images = 10
  for i in range(num_calibration_images):
    image = tf.random.normal([1] + list(image_shape))
    yield [image]converter=lite.TFLiteConverter.from_keras_model_file('model.h5')converter.default_ranges_stats=[0,255]
converter.optimizations = [tf.lite.Optimize.DEFAULT]
converter.representative_dataset = representative_dataset_genconverter.target_spec.supported_ops =[tf.lite.OpsSet.TFLITE_BUILTINS_INT8]converter.inference_input_type = tf.uint8
converter.inference_output_type = tf.uint8
model = converter.convert()
file = open( 'model.tflite' , 'wb' )
file.write(model)

---
### Testing with tFlite
imn = '/content/test/testimage.jpg'
img = load_img(imn, target_size=(256, 256))
img = np.asarray(img)
img = img.astype('float32')
img = img/255
img = np.expand_dims(img, axis=0)
img = img.reshape(1,256,256,3)interpreter = lite.Interpreter(model_path="model.tflite")
interpreter.allocate_tensors()# Get input and output tensors.
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()
interpreter.set_tensor(input_details[0]['index'], img)
interpreter.invoke()output_data = interpreter.get_tensor(output_details[0]['index'])
ord = np.argsort(output_data)
ind = np.argmax(output_data)lis = []
for i in range(0, 5):
  lis.append(li[(ord[0][19 - i])])
print(lis)

---

### Design Architecture 
# MODEL
* `adapter`  consists of recyclerviews adapters.<br>
* `Database` consists of dao(class for room database) ,UserAuthentication,db..<br>
-`DataAccessObject`<br>
-`AppDatabase`<br>


# `VIEW`
* `Data`  consists of all the datas i.e notification, session, task, user<br>
-`Screens` consists all the screens i.e addeditTask, login, menu, splash, tasks <br>



# `ViewModel`
* `FoodListActivityViewModel`  <br>
* `HomeViewModel` <br>
* `LoginViewModel` <br>
* `SplashViewModel` <br>

## Features
Sign-IN                   |  Register                    | Tracking Momo             |  Adding Momo to Journal
:----------------------------:|:--------------------------------------:|:----------------------:|:-----------------
<img src = "https://imgur.com/jwmeX9j.gif" width="200" height="360"> |<img src = "https://imgur.com/ZsHq55S.gif" width="200" height="360">|<img src = "https://imgur.com/F9sS5Gz.gif" width="200" height="360">|<img src = "https://imgur.com/mP3eVb0.gif" width="200" height="360">
 #
  
Checking journal         |  Logout                 
:----------------------------:|:--------------------------------------
 <img src = "https://imgur.com/tjZNaOK.gif" width="200" height="360"> |   <img src = "https://imgur.com/yDEFfnZ.gif" width="200" height="360">   
 ---



----------------------------

## Documentation (Model–view–viewmodel architecture in a nutshell) 
Model–view–viewmodel (MVVM) is a software architectural pattern that facilitates the separation of the development of the graphical user interface (the view) be it via a markup language or GUI code from the development of the business logic or back-end logic (the model) so that the view is not dependent on any specific model platform. The view model of MVVM is a value converter,meaning the view model is responsible for exposing (converting) the data objects from the model in such a way that objects are easily managed and presented. In this respect, the view model is more model than view, and handles most if not all of the view's display logic.The view model may implement a mediator pattern, organizing access to the back-end logic around the set of use cases supported by the view.
#
![](https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/img/a7da8f5ea91bac52.png)

---
## Documentation (Tensorflow architecture in a nutshell) 
Created by the Google Brain team, TensorFlow is an open source library for numerical computation and large-scale machine learning. TensorFlow bundles together a slew of machine learning and deep learning (aka neural networking) models and algorithms and makes them useful by way of a common metaphor. It uses Python to provide a convenient front-end API for building applications with the framework, while executing those applications in high-performance C++.


---
## Documentation (Keras in a nutshell) 
While deep neural networks are all the rage, the complexity of the major frameworks has been a barrier to their use for developers new to machine learning. There have been several proposals for improved and simplified high-level APIs for building neural network models, all of which tend to look similar from a distance but show differences on closer examination.

Keras is one of the leading high-level neural networks APIs. It is written in Python and supports multiple back-end neural network computation engines.

---
## Documentation (Firebase and Firebase Auth)
Firebase is Google’s mobile application development platform that helps you build, improve, and grow your app. Firebase Authentication provides backend services, easy-to-use SDKs, and ready-made UI libraries to authenticate users to your app. It supports authentication using passwords, phone numbers, popular federated identity providers like Google, Facebook and Twitter, and more.


