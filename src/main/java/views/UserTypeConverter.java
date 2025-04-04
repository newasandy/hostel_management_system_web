package views;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.UserType;

@FacesConverter("userTypeConverter")
public class UserTypeConverter implements Converter {


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        UserBean userBean = context.getApplication()
                .evaluateExpressionGet(context, "#{userBean}", UserBean.class);


        return userBean.getUserTypes().stream()
                .filter(type -> type.getId().toString().equals(value))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof UserType) {
            return ((UserType) value).getId().toString();
        } else {
            throw new IllegalArgumentException("Value is not a valid UserType: " + value);
        }
    }
}