#!/usr/bin/python
# -*- coding: utf-8 -*-  

from Utility  import *
from MbClient import MbClient

class BatchClient(MbClient):
    ''' 
     -----if ASYNC ------
    submit()
       |
       |
    progress() <--
       |        |
       |------->|
       |
    result()
    -------if SYNC ------
    submit()  - pending
    '''
    mode = "sync"
    
    def __init__(self, server, port, user, password, debug, mode):
        MbClient.__init__(self, server, port, user, password, debug)
        self.mode = mode
 
    def submit(self, token, sqls):
        req_url = self.url + "/submit"
        
        msqls = mkSql(sqls)
            
        msg_data = '"token":"{token}", "mode":"{mode}", "sqls":[{sqls}]'.format(token=token, mode=self.mode, sqls=msqls)
        msg_data = "{" + msg_data + "}"
        code, json_string = sendToService(req_url, msg_data)
        if self.mode == "sync":
            return json_string['jobId'], json_string['schema'], json_string['data']
        else:
            return json_string['jobId']


    def progress(self, token, job_id):
        req_url = self.url + "/progress"
        msg_data = '"token":"{token}", "jobId":"{jobId}"'.format(token=token, jobId=job_id)
        msg_data = "{" + msg_data + "}"
        code, json_string = sendToService(req_url, msg_data)
        return json_string['jobId'], json_string['status']
            
            
    def result(self, token, job_id, offset, size):
        req_url = self.url + "/result"
        msg_data = '"token":"{token}", "jobId":"{jobId}", "offset":{offset}, "size": {size}'\
            .format(token=token, jobId=job_id, offset=offset, size=size)
        msg_data = "{" + msg_data + "}"
        code, json_string = sendToService(req_url, msg_data)
        return json_string['jobId'], json_string['schema'], json_string['data']
        
    def process(self, sqls):
        token = self.login()
        
        if self.mode=="sync":
            job_id, schema, data = self.submit(token, sqls)
            print_result(schema, data)
        else:
            job_id = self.submit(token, sqls)
            job_id, status = self.progress(token, job_id)
            while status == "WAITING" or status == "RUNNING":
                time.sleep(1)
                job_id, status = self.progress(token, job_id)

            if status == "SUCCESS":
                job_id, schema, data = self.result(token, job_id, 0, -1)
                print_result(schema, data)
            else:
                print("error progress ", status)
        
        self.logout(token)
    