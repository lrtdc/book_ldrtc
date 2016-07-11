package org.light.cxf;

import javax.jws.WebService;

@WebService(endpointInterface = "org.light.cxf.HelloWS", portName = "HelloWSPort",
        serviceName = "HelloWSService", targetNamespace = "http://www.tmp.com/services/hello")
public class HelloWSImpl implements HelloWS {
    @Override
    public String welcome(String name) {
        return "Welcome " + name;
    }

}
