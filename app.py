import json

from flask import Flask, request
import os
from copy import deepcopy
import smtplib

PASSWORD = 'pass'

USER = 'user'

app = Flask(__name__)

empty_email = {'last_name':'','first_name':'','display_name':'','email_address':'','smtp_host':'','tls_port':'','email_password':''}


data = {'test': {'password': 'password', 'history': {}, 'userinfo':deepcopy(empty_email)}}
if not os.path.exists('data.json'):
    f = open('data.json', 'w')
    f.write(json.dumps(data, indent=4))
    f.close()
else:
    f = open('data.json', 'r')
    data = json.loads(f.read())
    f.close()

@app.route('/emails', methods = ['GET', 'PUT'])
def emails():
    if request.method == 'PUT':
        if request.args.get(USER) in data:
                if request.args.get(PASSWORD) == data[request.args.get(USER)]['password']:
                    data[request.args.get(USER)]['userinfo'] = request.get_json(force=True)
                    write()
                    return 'Placed'
                else:
                    return 'Incorrect'
    elif request.method == 'GET':
        if request.args.get(USER) in data:
            if request.args.get(PASSWORD) == data[request.args.get(USER)]['password']:
                return json.dumps(data[request.args.get(USER)]['userinfo'])
            else:
                return 'Incorrect'

@app.route('/question', methods=['GET', 'PUT', 'POST', 'DELETE'])
def questions():
    if request.method == 'GET':
        if 'new' in request.args:
            if request.args.get(USER) not in data:
                print(request.args.get(USER))
                data[request.args.get(USER)] = {'password': request.args.get(PASSWORD), 'history': {}}
                write()
                return 'True'
            else:
                return 'Taken'
        elif request.args.get(USER) in data:
            if request.args.get(PASSWORD) == data[request.args.get(USER)]['password']:
                return json.dumps(data[request.args.get(USER)]['history'])
            else:
                return 'Incorrect'
        else:
            return 'Incorrect'
    elif request.method == 'PUT' or request.method == 'DELETE':
        if request.args.get(USER) in data:
            if request.args.get(PASSWORD) == data[request.args.get(USER)]['password']:
                data[request.args.get(USER)]['history'] = request.get_json(force=True)
                write()
                return 'Placed'
            else:
                return 'Incorrect'
    elif request.method == 'POST':
        requested_u = request.args.get(USER)
        if requested_u not in data:
            data[requested_u] = {'password': request.args.get(PASSWORD), 'history': {},'userinfo':deepcopy(empty_email)}
            write()
            return 'Created'
        else:
            return 'Taken'

@app.route('/send', methods=['POST'])
def send():
    if request.args.get(USER) in data:
        if request.args.get(PASSWORD) == data[request.args.get(USER)]['password']:
            uinfo = data[request.args.get(USER)]['userinfo']
            port = uinfo["tls_port"]
            smtp_server = uinfo["smtp_host"]
            sender_email = uinfo["email_address"]
            receiver_email = request.args.get("destination")
            password = uinfo["email_password"]
            res = 'init'

            try:
                server = smtplib.SMTP(smtp_server,port, timeout = 3)
                server.ehlo()
                server.starttls()
                server.login(sender_email, password)
                server.sendmail(sender_email, receiver_email, request.data)
                res = f"Email successfully sent to {receiver_email}!"
            except Exception as e:
                res = e
            finally:
                server.quit()
            return res



def write():
    t = open('data.json', 'w')
    t.write(json.dumps(data, indent=4))


if __name__ == '__main__':
    app.run()
