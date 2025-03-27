package views;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import daoImp.UserTypeDAOImp;
import daoInterface.UserTypeDAO;
import model.UserType;

@FacesConverter("userTypeConverter")
public class UserTypeConverter implements Converter {
    private UserTypeDAO userTypeDAO = new UserTypeDAOImp();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        if (value == null || value.isEmpty()) {
            return null;
        }

        Long id = Long.valueOf(value);
        return userTypeDAO.getById(id);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        UserType userType = (UserType) value;
        return userType.getId().toString();
    }
}