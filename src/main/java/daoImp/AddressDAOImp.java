package daoImp;

import model.Address;

import javax.ejb.Stateless;
import java.io.Serializable;


@Stateless
public class AddressDAOImp extends BaseDAOImp<Address> implements Serializable {
    public AddressDAOImp (){
        super(Address.class);
    }
}
