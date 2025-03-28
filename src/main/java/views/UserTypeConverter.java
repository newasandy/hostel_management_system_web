package views;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import daoImp.UserTypeDAOImp;
import daoInterface.UserTypeDAO;
import model.UserType;

@FacesConverter("userTypeConverter")
public class UserTypeConverter implements Converter {
    private UserTypeDAO userTypeDAO = new UserTypeDAOImp();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            UserType type = userTypeDAO.getById(id);
            if (type == null) {
                throw new ConverterException("UserType not found with ID: " + value);
            }
            return type;
        } catch (NumberFormatException e) {
            throw new ConverterException("Invalid UserType ID format: " + value, e);
        }
    }
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof UserType) {
            return ((UserType) value).getId().toString();
        } else {
            throw new ConverterException("Value is not a valid UserType: " + value);
        }
    }
}