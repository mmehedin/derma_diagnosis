import sys, os
import numpy as np
import tensorflow as tf
from datetime import datetime
import cv2
import glob
from keras.models import Sequential
from keras.models import model_from_json
from keras.layers import Convolution2D
from keras.layers import MaxPooling2D
from keras.layers import Dense
from keras.layers import Flatten
from matplotlib import figure
from keras.layers import Activation, Dropout
from matplotlib import pyplot as plt
import json


input_image_size = (128, 128, 3)
filter_size = 32
pool_size = (2,2)
output_size = 2

def prepareImage(im):
    top=0
    bottom=0
    left=0
    right=0

    (y_shape, x_shape, z_shape) = im.shape
    thres = x_shape - y_shape

    if(thres > 0):
        top = thres // 2
        bottom = thres - top
    elif(thres < 0):
        thres = abs(thres)
        left = thres // 2
        right = thres - top

    (r,g,b) = im[1].mean(axis=0)
    border=cv2.copyMakeBorder(im, top=top,
                          bottom=bottom, left=left, right=right,
                          borderType= cv2.BORDER_CONSTANT, value=[r,g,b])
    res_im = cv2.resize(border, (input_image_size[0], input_image_size[1]))
    return res_im

def loadModel():
    base_path = ".\..\models\model1\"
    # load json and create model
    json_file = open(base_path + 'model.json', 'r')
    loaded_model_json = json_file.read()
    json_file.close()
    model = model_from_json(loaded_model_json)
    # load weights into new model
    model.load_weights(base_path + "model.h5")
    print("Model loaded")
    return model

loaded_model = loadModel()

def predict(path):
    if(loaded_model == None):
        loadModel()
    im = cv2.imread(path)
    p_img = prepareImage(im)
    p_img = p_img / 255
    result = loaded_model.predict(np.array([p_img]))
    if(result[0][0] > result[0][1]):
        return "Benign"
    else:
        return "Malign"

if __name__ == '__main__':
    print(predict('./../ISIC-images/UDA-1/ISIC_0000544.jpg'))