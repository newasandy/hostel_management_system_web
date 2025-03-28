package daoImp;

import model.Address;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class AddressDAOImp extends BaseDAOImp<Address>  {
    public AddressDAOImp (){
        super(Address.class);
    }
}
