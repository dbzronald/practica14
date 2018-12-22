package com.web.practica14.UI;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.web.practica14.Entity.Event;
import com.web.practica14.Services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.calendar.CalendarItemTheme;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@SpringComponent
@UIScope
public class UIEvent extends VerticalLayout {

    DatePicker date = new DatePicker();
    TextField txt = new TextField("Title");

    public UIEvent(
            @Autowired EventService eventoService) {

        FormLayout formLayout = new FormLayout();

        H2 header = new H2("Add a new event");

        date.setLabel("Set date");
        date.setValue(LocalDate.now());

        Button add = new Button("Add");
        add.setIcon(new Icon(VaadinIcon.CALENDAR_O));
        add.getElement().setAttribute("theme", "primary");

        Button edit = new Button("Edit");
        edit.setIcon(new Icon(VaadinIcon.EDIT));
        edit.getElement().setAttribute("theme", "warning");

        Button canc = new Button("Cancel");
        canc.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        canc.getElement().setAttribute("theme", "error");

        HorizontalLayout btns = new HorizontalLayout(add, canc);

        formLayout.add(txt, date);
        setAlignItems(Alignment.CENTER);

        add(header, formLayout, btns);

        canc.addClickListener((evento) -> {
            txt.setValue("");
            date.setValue(LocalDate.now());
        });

        add.addClickListener((evento) -> {

            Event e = new Event(
                    eventoService.listarEventos().size() + 1,
                    Date.from(date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    txt.getValue(),
                    CalendarItemTheme.Green
            );
            eventoService.crearEvento(
                    e.getId(),
                    e.getDate(),
                    e.getTitle(),
                    e.getColor()
            );

            txt.setValue("");
            date.setValue(LocalDate.now());

            UIMain.calendario.refresh();
        });

    }
}
