package com.web.practica14.UI;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.web.practica14.Entity.Event;
import com.web.practica14.Entity.User;
import com.web.practica14.Services.EventService;
import com.web.practica14.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import org.vaadin.calendar.CalendarComponent;
import org.vaadin.calendar.CalendarItemTheme;
import org.vaadin.calendar.data.AbstractCalendarDataProvider;
import org.w3c.dom.events.UIEvent;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Route("calendario")
@SpringComponent
@UIScope
public class UIMain extends VerticalLayout {

    public static CalendarComponent<Event> calendario = new CalendarComponent<Event>()
            .withItemDateGenerator(Event::getDate)
            .withItemLabelGenerator(Event::getTitle)
            .withItemThemeGenerator(Event::getColor);

    @Autowired
    public static EventService eventService;

    @Autowired
    public UIMain(
            @Autowired com.web.practica14.UI.UIEvent uiEvent,
            @Autowired UserService userService,
            @Autowired EventService eventService,
            @Autowired UIEmail uiEmail,
            @Autowired ModifyEvent modifyEvent) {
        UIMain.eventService = eventService;

        if (userService.listarUsuarios().isEmpty()) {
            getUI().get().navigate("");
        } else if (!userService.listarUsuarios().get(0).isLogged()) {
            getUI().get().navigate("");
        } else {
            setAlignItems(Alignment.CENTER);

            HorizontalLayout btns = new HorizontalLayout();
            btns.setSpacing(true);

            Button add = new Button("Add event");
            add.setIcon(new Icon(VaadinIcon.CALENDAR));
            add.getElement().setAttribute("theme", "primary");

            Button sendMail = new Button("Send Email");
            sendMail.setIcon(new Icon(VaadinIcon.CALENDAR_ENVELOPE));

            Button vUser = new Button("Settings");
            vUser.setIcon(new Icon(VaadinIcon.COG_O));

            Button geren = new Button("Gerentes");
            geren.setIcon(new Icon(VaadinIcon.CLIPBOARD_USER));

            Button ex = new Button("Exit");
            ex.setIcon(new Icon(VaadinIcon.SIGN_OUT));
            ex.getElement().setAttribute("theme", "error");

            cButton(add, uiEvent);
            cButton(sendMail, uiEmail);

            btns = new HorizontalLayout(add, sendMail, vUser, geren, ex);

            ex.addClickListener((evento) -> {

                try {
                    User usuario = userService.listarUsuarios().get(0);
                    usuario.setLogged(false);
                    userService.editarUsuario(usuario);

                    getUI().get().navigate("");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            vUser.addClickListener((evento) -> getUI().get().navigate("usuario"));
            geren.addClickListener((evento) -> getUI().get().navigate("gerentes"));

            eventService.crearEvento(
                    1,
                    Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    "Practica 14",
                    CalendarItemTheme.Green
            );

            calendario.setDataProvider(new CustomDataProvider());

            calendario.addEventClickListener(evt -> {
                try {
                    modifyEvent.fecha.setValue(evt.getDetail().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    modifyEvent.titulo.setValue(evt.getDetail().getTitle());

                    uDialog(modifyEvent);

                    eventService.crearEvento(evt.getDetail().getId(), evt.getDetail().getDate(), evt.getDetail().getTitle(), evt.getDetail().getColor());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            H2 h2 = new H2("Vaadin Calendar");
            H4 h4 = new H4("Welcome");

            setAlignItems(Alignment.CENTER);

            add(h2, h4, btns, calendario);

        }

        Button add = new Button("Add");
        add.setIcon(new Icon(VaadinIcon.PLUS));
        add.getElement().setAttribute("theme", "primary");
    }

    private void uDialog(VerticalLayout form) {
        Dialog ui = new Dialog();
        ui.add(form);

        ui.open();
    }

    private void cButton(Button btn, VerticalLayout form) {
        btn.addClickListener((e) -> {
            uDialog(form);
        });
    }
}

@SpringComponent
@UIScope
class CustomDataProvider extends AbstractCalendarDataProvider<Event> {
    @Override
    public Collection<Event> getItems(Date fromDate, Date toDate) {
        List<Event> eventos = UIMain.eventService.listarEventos();
        return eventos;
    }
}
