package com.web.practica14.UI;

import com.sendgrid.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.io.IOException;

@SpringComponent
@UIScope
public class UIEmail extends VerticalLayout {

    TextField to = new TextField("To:");
    TextField subj = new TextField("Subject:");
    TextArea body = new TextArea("Body:");

    public UIEmail() {

        FormLayout fm = new FormLayout();

        H2 header = new H2("Send Email");

        Button send = new Button("Send");
        send.setIcon(new Icon(VaadinIcon.ARROW_FORWARD));
        send.getElement().setAttribute("theme", "primary");

        Button canc = new Button("Cancel");
        canc.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE_O));
        canc.getElement().setAttribute("theme", "error");

        HorizontalLayout btns = new HorizontalLayout(send, canc);

        fm.add(to, subj, body);
        setAlignItems(Alignment.CENTER);

        add(header, fm, btns);

        send.addClickListener((evento) -> {

            Email fromE = new Email("test@example.com");
            Email toE = new Email(to.getValue());

            String subjE = to.getValue();
            Content bodyE = new Content("text/plain", body.getValue());

            Mail email = new Mail(fromE, subjE, toE, bodyE);

            String apiKey = "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";

            SendGrid sendg = new SendGrid(apiKey);
            Request request = new Request();

            request.method = Method.POST;
            request.endpoint = "mail/send";

            try {
                request.body = email.build();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Response response = null;
            try {
                response = sendg.api(request);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(response.statusCode);
            System.out.println(response.body);
            System.out.println(response.headers);

            to.setValue("");
            subj.setValue("");
            body.setValue("");

        });

        canc.addClickListener((evento) -> {
            to.setValue("");
            subj.setValue("");
            body.setValue("");
        });
    }
}
