package com.web.practica14.UI;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.web.practica14.Entity.User;
import com.web.practica14.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("usuario")
@SpringComponent
@UIScope
public class UIUser extends VerticalLayout {

    public UIUser(
            @Autowired UserService userService) {

        User usuario;

        VerticalLayout verL;
        HorizontalLayout buttons;
        FormLayout editInfo;

        if (userService.listarUsuarios().isEmpty())
            getUI().get().navigate("");

        else if (!userService.listarUsuarios().get(0).isLogged())
            getUI().get().navigate("");

        else {
            usuario = userService.listarUsuarios().get(0);

            verL = new VerticalLayout();
            buttons = new HorizontalLayout();

            verL.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

            H2 h2 = new H2("Vaadin Calendar");
            H4 h4 = new H4("User Profile");

            Button calend = new Button("Back");
            calend.setIcon(new Icon(VaadinIcon.ARROW_CIRCLE_LEFT_O));

            Button ex = new Button("Exit");
            ex.setIcon(new Icon(VaadinIcon.SIGN_OUT));
            ex.getElement().setAttribute("theme", "error");

            ex.addClickListener((evento) -> {

                try {
                    User usuarioaux = userService.listarUsuarios().get(0);
                    usuarioaux.setLogged(false);
                    userService.editarUsuario(usuarioaux);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                getUI().get().navigate("");
            });

            calend.addClickListener((evento) -> getUI().get().navigate("calendario"));

            FormLayout fml = new FormLayout();

            VerticalLayout edV = new VerticalLayout();
            editInfo = new FormLayout();

            H3 h3 = new H3("User settings");

            TextField nMail = new TextField("Email");
            nMail.setPlaceholder(usuario.getMail());
            TextField nName = new TextField("Name");
            nName.setPlaceholder(usuario.getName());

            Button save = new Button("Save");
            save.setIcon(new Icon(VaadinIcon.CHECK));

            editInfo.add(h3, nName, nMail);
            edV.add(h3, editInfo, save);

            save.addClickListener((evento) -> {

                try {
                    if (!nMail.getValue().equals(""))
                        usuario.setMail(nMail.getValue());

                    if (!nName.getValue().equals(""))
                        usuario.setName(nName.getValue());

                    userService.editarUsuario(usuario);
                    getUI().get().getPage().reload();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            verL.add(fml, edV);
            verL.setAlignItems(Alignment.CENTER);

            buttons.add(calend, ex);

            add(h2, h4, buttons, verL);
        }
    }
}

