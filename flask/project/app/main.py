from flask import Flask, jsonify, request
import flask
from app import app as application
import joblib

app = Flask(__name__)

@app.route('/')
def main_page():
    return "this page is main page"

    
@app.route('/item/predict', methods=["GET","POST"])
def preddict():
    model = joblib.load('app/item_model.pkl') 
    pred_list = []
    recommend_dict = {}
    list_top_5 = []
    list_data = ['Ornaments', 'Tire', 'Dash cam', 'Bags', 'Air Freshener', 'Tools', 'Wind Shield', 'Headrest', 'Chewing Gum', 'Glove Box', 'Tumbler', 'Wiper', 'Car Phone Holder', 'Trunk', 'Drinks Holders', 'Snow Chain', 'Car Mats', 'Seat Covers', 'Car wheel', 'Charger', 'Puncture Patch', 'Tail Light', 'Car Vacuum Cleaner', 'Blanket', 'Sun Shade', 'Clocks', 'Engine Oil', 'Car Stickers', 'Parking Ticket', 'Hood'] 
    data = {"success": False}
    params = flask.request.args
    print("params", params)
    return_itme = {}
    
    if (params != None):

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
    application.run(host='0.0.0.0',debug=True, port=80)
