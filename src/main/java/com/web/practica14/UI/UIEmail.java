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

            Email fromE = new Email("vaadin@practica14.com");
            Email toE = new Email(to.getValue());

            String subjE = to.getValue();
            Content bodyE = new Content("text/plain", body.getValue());

            Mail email = new Mail(fromE, subjE, toE, bodyE);

            String apiKey = "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";

            SendGrid sendg = new SendGrid(apiKey);
            Request request = new Request();

            try {
                request.method = Method.POST;
                request.endpoint = "mail/send";
                request.body = email.build();
                Response response = sendg.api(request);

                System.out.println(response.statusCode);
                System.out.println(response.body);
                System.out.println(response.headers);

                to.setValue("");
                subj.setValue("");
                body.setValue("");

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        canc.addClickListener((evento) -> {
            to.setValue("");
            subj.setValue("");
            body.setValue("");
        });
    }
}
