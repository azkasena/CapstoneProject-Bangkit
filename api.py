import os
from datetime import timedelta
from flask import Flask,jsonify,request
#from util import Preprocessing
from flask_jwt_extended import JWTManager,create_access_token,jwt_required,get_jwt_identity
from database import init,getalluser,registerdb,logindb
from flask_cors import CORS
from waitress import serve
from flask_swagger_ui import get_swaggerui_blueprint
import tensorflow as tf
import json
import numpy as np
import pandas as pd
import base64
from google.cloud import storage
import urllib.request

#init flask and sql
app = Flask(__name__)
#client = storage.Client.from_service_account_json('service_account.json')
#bucket_name = 'nama_bucket'
#model_file_name = 'model.h5'
#bucket = client.get_bucket(bucket_name)
#blob = bucket.blob(model_file_name)
#blob.download_to_filename('model.h5')
model_url = 'https://storage.googleapis.com/healthy-teeth/model.h5'
urllib.request.urlretrieve(model_url, 'model.h5')
model = tf.keras.models.load_model('model.h5')
CORS(app)
mysql = init(app)

#JWT
app.config["JWT_SECRET_KEY"] = "capstone-secret-key" 
app.config["JWT_ACCESS_TOKEN_EXPIRES"] = timedelta(days=90)
jwt = JWTManager(app)




@app.route('/', methods=['GET'])
def index():
    #current_user = get_jwt_identity()
    #return jsonify(logged_in_as=current_user), 200
    return "Hello World"

@app.route('/login', methods=['POST'])
def login():
    email = request.form['email']
    pwd = request.form['password']
    try:
        user = logindb(mysql,email,pwd)
        if user != "":
            access_token = create_access_token(identity=email)
            data = {"message": "Login Successful" , "user": user, "access_token" : access_token}
            return jsonify(data),200
        return jsonify({"msg": "Wrong Email or Password"}), 401
    except Exception as e:
         err = jsonify(msg=f'{e}'),500
         return err
    
@app.route('/register', methods=['POST'])
def register():
    username = request.form['name']
    email = request.form['email']
    pwd = request.form['password']
    try:
        return registerdb(mysql,username,email, pwd)
    except Exception as e:
         err = jsonify(msg=f'{e}'),500
         return err
 
@app.route('/users',methods=['GET'])
def userlist():
    try:
        user = getalluser(mysql)
        return jsonify(user)
    except Exception as e:
         err = jsonify(msg=f'{e}'),500
         return err

@app.route("/predict", methods=['POST'])
@jwt_required()
def predict():
    file = open('labels.txt', 'r')
    labels = file.read().splitlines()
    image = generate_image_from_base64(
        request.form["title"], request.form["body"])
    img = tf.keras.preprocessing.image.load_img(image, target_size=(224, 224))
    x = tf.keras.preprocessing.image.img_to_array(img)
    x = np.expand_dims(x, axis=0)
    images = np.vstack([x])
    history = model.predict(images)[0]
    print(history)
    classes = int(history.argmax(axis = -1))
    os.remove(get_filename(request.form["title"]))
    return {
        "diagnosis": labels[classes]
    }

def generate_image_from_base64(filename, string):
    file = open('./{filename}.jpg'.format(filename=filename), 'wb')
    file.write(base64.b64decode((string)))
    file.close()

    return './{filename}.jpg'.format(filename=filename)
def get_filename(filename):
    return './{filename}.jpg'.format(filename=filename)


if __name__ == '__main__':
    #serve(app, host="0.0.0.0", port=int(os.environ.get('PORT', 80)))
    app.run(port=5000, debug=True, host='localhost', use_reloader=True)
    print("Running on port 80")
    #app.run(host="0.0.0.0", port=int(os.environ.get('PORT', 80)))
    #$Env:PORT=4000