package daoImp;

import model.Address;

import javax.enterprise.context.Dependent;
import java.io.Serializable;


@Dependent
public class AddressDAOImp extends BaseDAOImp<Address> implements Serializable {
    public AddressDAOImp (){
        super(Address.class);
    }
}
