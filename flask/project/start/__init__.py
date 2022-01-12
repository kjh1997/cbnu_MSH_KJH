from flask import Flask, jsonify, request
import flask
import joblib
import os
app = Flask(__name__)

@app.route('/')
def main_page():
    return "this page is main page"
@app.route('/item/predict', methods=["GET","POST"])
def preddict():
    model = joblib.load('start/item_model.pkl') 
    f = open("start/item_list.txt", 'r')
    data = f.read()
    pred_list = []
    recommend_dict = {}
    list_top_5 = []
    list_data = data.split(',')
    data = {"success": False}
    params = flask.request.args
    print("params", params)
    return_itme = {}
    
    if (params != None):
        print("xxx2",params)

        for i in list_data:
            pred_list.append(model.predict(params['id'], i))
        for i in pred_list:
            recommend_dict[i[1]] = i[3]
        sorted_dict = sorted(recommend_dict.items(), key = lambda item: item[1], reverse = True)
        
        for i in sorted_dict[:5]:
            list_top_5.append(i)
        return_itme['item'] = list_top_5
    
    return  flask.jsonify(return_itme)

if __name__ == '__main__':

    app.run(host='127.0.0.1', port=8080)
