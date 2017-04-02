package Services;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ISStatusServiceInterface {
    @WebMethod
    String[] statusesToBean(Integer[] ids);
}
