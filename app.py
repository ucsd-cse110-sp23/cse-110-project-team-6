import json

from flask import Flask, request
import os

PASSWORD = 'pass'

USER = 'user'

app = Flask(__name__)

data = {'test': {'password': 'password', 'history': {}}}
if not os.path.exists('data.json'):
    f = open('data.json', 'w')
    f.write(json.dumps(data, indent=4))
else:
    f = open('data.json', 'r')
    data = json.loads(f.read())
    f.close()


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
        if request.args.get(USER) in data:
            if request.args.get(PASSWORD) == data[request.args.get(USER)]['password']:
                return json.dumps(data[request.args.get(USER)]['history'])
            else:
                return 'Incorrect'

    # I don't know why we even have DELETE if it does the same thing
    elif request.method == 'PUT' or request.method = 'DELETE':
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
            data[requested_u] = {'password': request.args.get(PASSWORD), 'history': {}}
            write()
            return 'Created'
        else:
            return 'Taken'


def write():
    t = open('data.json', 'w')
    t.write(json.dumps(data, indent=4))


if __name__ == '__main__':
    app.run()
