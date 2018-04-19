#!/usr/bin/python
# -*- coding: utf-8 -*-  

from Utility  import *
from MbClient import MbClient

class AdhocClient(MbClient):
    '''
    open_session
      |
    query()  - pending
      |
    close_session
    '''
    def __init__(self, server, port, user, password, debug):
        MbClient.__init__(self, server, port, user, password, debug)
        

    def open_session(self, token):
        req_url = self.url + "/openSession"
        msg_data = '"token": "{token}"'.format(token = token)
        msg_data = "{" + msg_data + "}"
        
        code, json_string = sendToService(req_url, msg_data)
        return json_string['sessionId']

    def close_session(self, token, sessionId):
        req_url = self.url + "/closeSession"
        msg_data = '"token": "{token}", "sessionId": "{sessionId}"'.format(token = token, sessionId = sessionId)
        msg_data = "{" + msg_data + "}"
        
        code, json_string = sendToService(req_url, msg_data)
        return None
        
        
    def query(self, token, sessionId, sqls):
        msqls = mkSql(sqls)
        
        req_url = self.url + "/query"
        msg_data = '"token": "{token}", "sessionId": "{sessionId}", "sqls": [{sqls}]'.format(token = token, sessionId = sessionId, sqls = msqls)
        msg_data = "{" + msg_data + "}"
        
        code, json_string = sendToService(req_url, msg_data)
        return json_string['jobId'], json_string['schema'], json_string['data']
    
    def process(self, sqls):
        token = self.login()
        
        sessionId = self.open_session(token)
        job_id, schema, data = self.query(token, sessionId, sqls)
        print_result(schema, data)
        
        self.close_session(token, sessionId)
    
        self.logout(token)
        
        