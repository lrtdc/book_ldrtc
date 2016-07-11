package org.light.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "HelloWS", targetNamespace = "http://www.tmp.com/services/hello")
public interface HelloWS {
    @WebMethod
    String welcome(@WebParam(name = "name") String name);
}
