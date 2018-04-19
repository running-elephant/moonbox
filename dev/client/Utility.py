#!/usr/bin/python
# -*- coding: utf-8 -*-  

import random
import time
import datetime
import types
import json
import urllib2
from urllib2 import Request, URLError, HTTPError 
import sys
import string
import logging

logger = logging.getLogger("moonbox")
formatter = logging.Formatter('%(asctime)s %(levelname)-8s: %(message)s')
console_handler = logging.StreamHandler(sys.stdout)
console_handler.formatter = formatter  # 也可以直接给formatter赋值
logger.addHandler(console_handler)
def setLevel(debug):
    if debug == "True":
        logger.setLevel(logging.INFO)
    else:
        logger.setLevel(logging.ERROR)

def show(*str):
    print str,
    
def info(*str):
    logger.info(str)
    
def error(*str):
    logger.error(str)
    
def warn(*str):
    logger.warn(str)
    
def critical(*str):
    logger.critical(str)    
    

def print_result(schema, data):
    #print(schema)
    #print(data)
    
    jobject = json.loads(schema)
    for obj in jobject['fields']:
        print "%s"% (obj['metadata']['name']+ "(" + obj['type'] + ")"), #print "%-8.8s"%(...)
        print " | ",
    print("")
    
    for row in data:
        for item in row:
            print "%-.12s"%item ,
            print" | ",
        print ""
    print ""
    
    
def mkSql(sqls):
    msqls=""
    mlist = sqls.split(';')
    for i in range(len(mlist)):
        msqls+='"' + mlist[i] + '"'
        if i + 1 < len(mlist):
            msqls += ','
    return msqls
    
    
def sendToService(req_url, msg_data):
    try:
        #encode_data = urllib.parse.urlencode(msg_data).encode("UTF8")
        info(msg_data)
        request = urllib2.Request(req_url, msg_data)
 
        request.get_method = lambda: 'POST'
        rsp_msg = urllib2.urlopen(request, timeout=5)
        rsp_body = rsp_msg.read()
        info(rsp_body)
        info(rsp_msg.code)
        s = json.loads(rsp_body)
         # print s["created"]
        return rsp_msg.code, s
    except URLError as rsp_msg:
        if hasattr(rsp_msg, 'reason'):
            error('We failed to reach a server.', req_url, msg_data)
            error('Reason: ', rsp_msg.reason)
            error("")
        elif hasattr(rsp_msg, 'code'):
            error('The server couldn not fulfill the request.',req_url, msg_data)
            error('Error code: ', rsp_msg.code)
            error("")
        raise rsp_msg
        #return 0, False
    except Exception as e:
        error('Failed to send, reason: ', e)
        raise e
        #return 1, False
        
        