package daoImp;


import model.Address;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;


@Named
@ApplicationScoped
public class AddressDAOImp extends BaseDAOImp<Address> implements Serializable {
    private static final long serialVersionUID = 1L;
    public AddressDAOImp (){
        super(Address.class);
    }
}
