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

from optparse import OptionParser

from Utility  import *
from BatchClient import BatchClient
from AdhocClient import AdhocClient

def main():
    ''' support for python 2.7
    Usage: RestTest.py [options]
    Options:
      -h, --help    ;show this help message and exit
      -s SERVER, --server=SERVER  ;server
      -p PORT, --port=PORT  ;port
      -u USER, --user=USER  ;username
      -P PASSWORD, --password=PASSWORD ;password
      -S SQL, --sql=SQL     ;sql
      -m MODE, --mode=MODE  ;mode
      -M METHOD, --method=METHOD   ;method
      -d DEBUG, --debug=DEBUG  ;debug
    '''
    start_time = datetime.datetime.now()
    print("begin to communicate server %s ... " % start_time)

    parser = OptionParser()
    parser.add_option("-s", "--server", type="string", dest="server", default="localhost", help="server")
    parser.add_option("-p", "--port", type="string", dest="port", default="8080", help="port")
    parser.add_option("-u", "--user", type="string", dest="user", default=None,  help="username")
    parser.add_option("-P", "--password", type="string", dest="password", default=None, help="password")
    parser.add_option("-S", "--sql",  type="string", dest="sql", default=None, help="sql")
    parser.add_option("-m", "--mode", type="string", dest="mode", default="sync", help="mode")
    parser.add_option("-M", "--method", type="string", dest="method", default="batch", help="method")
    parser.add_option("-d", "--debug",  type="string", dest="debug", default="False", help="debug")
    parser.add_option("-t", "--timeout",  type="string", dest="timeout", default="60", help="timeout")

    (options, args) = parser.parse_args(args=None, values=None)
    
    
    server = options.server
    port = options.port
    user = options.user
    password = options.password
    sql = options.sql
    mode = options.mode
    method = options.method
    debug = options.debug
    ts = options.timeout
    setLevel(debug)
    setTimeout(ts)

    
    if method == "batch":
        client = BatchClient(server, port, user, password, debug, mode)
    else:
        client = AdhocClient(server, port, user, password, debug)
    
    client.process(sql)

    end_time = datetime.datetime.now()
    print("end to communicate server %s, Elapse [ %d ] ms ... " %(end_time, (end_time - start_time).microseconds / 1000))
    
    
if __name__ == "__main__":
    main()
    
    
    