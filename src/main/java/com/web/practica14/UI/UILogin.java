package com.web.practica14.UI;


import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.web.practica14.Entity.User;
import com.web.practica14.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;


import javax.persistence.PersistenceException;

@Route("")
@SpringComponent
@UIScope

public class UILogin extends VerticalLayout {

    public UILogin(
            @Autowired UserService userService) {

        TextField email = new TextField("Email");
        PasswordField passw = new PasswordField("Password");
        TextField name = new TextField("Name");

        Button btnLogin = new Button("Enter");

        btnLogin.setIcon(new Icon(VaadinIcon.SIGN_IN_ALT));
        btnLogin.getElement().setAttribute("theme", "primary");
        VerticalLayout vertL = new VerticalLayout();
        vertL.setHeight("100%");
        vertL.setSizeFull();

        vertL.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        H2 title = new H2("Practica 14 - Calendario Vaadin");
        H4 title2 = new H4("Registrate para comenzar");


        if (userService.listarUsuarios().isEmpty()) {
            vertL = new VerticalLayout(name, email, passw);

        } else {
            vertL = new VerticalLayout(email, passw);
        }

        btnLogin.addClickListener((evento) -> {
            if (userService.listarUsuarios().isEmpty()) {

                try {
                    userService.crearUsuario(userService.listarUsuarios().size() + 1,
                            name.getValue(), email.getValue(), passw.getValue());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                getUI().get().getPage().reload();


            } else {
                if (userService.validarUsuario(email.getValue(), passw.getValue())) {

                    try {
                        User usuario = userService.listarUsuarios().get(0);
                        usuario.setLogged(true);
                        userService.editarUsuario(usuario);
                        getUI().get().navigate("calendario");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    getUI().get().getPage().reload();
                }
            }
        });

        add(title, title2, vertL, btnLogin);
    }
}
