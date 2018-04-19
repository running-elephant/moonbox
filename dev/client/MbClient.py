#!/usr/bin/python
# -*- coding: utf-8 -*-  

from Utility import *

class MbClient:

    def __init__(self, server, port, user, password, debug):
        self.server = server
        self.port = port
        self.user = user
        self.password = password
        self.debug = debug
        self.url = "http://%s:%s" % (server, port)
        
    
    def login(self):
        req_url = self.url + "/login"
        msg_data = '"username": "{user}", "password": "{password}"'.format(user=self.user, password=self.password)
        msg_data = "{" + msg_data + "}"
        
        code, json_string = sendToService(req_url, msg_data)
        return json_string['token']
        

    def logout(self, token):
        req_url = self.url + "/logout"
        msg_data = '"token":"{token}", "sessionId":""'.format(token=token)
        msg_data = "{" + msg_data + "}"
        code, json_string = sendToService(req_url, msg_data)
        return json_string['message']
    
    def process(self, sqls): pass