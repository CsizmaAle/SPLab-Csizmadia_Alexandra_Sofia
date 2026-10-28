package com.example.demo;

// import com.example.demo.models.Author;
// import com.example.demo.models.Book;
// import com.example.demo.models.BookStatistics;
// import com.example.demo.models.GenerateToC;
// import com.example.demo.models.Image;
// import com.example.demo.models.ImageProxy;
// import com.example.demo.models.Paragraph;
// import com.example.demo.models.RenderContentVisitor;
// import com.example.demo.models.Section;
// import com.example.demo.models.Table;

import com.example.demo.difexamples.ClientComponent;
import com.example.demo.difexamples.SingletonComponent;
import com.example.demo.difexamples.TransientComponent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws Exception {        

        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);

        TransientComponent transientBean = context.getBean(TransientComponent.class);
        transientBean.operation();

        transientBean = context.getBean(TransientComponent.class);
        transientBean.operation();

        SingletonComponent singletonBean = context.getBean(SingletonComponent.class);
        singletonBean.operation();

        singletonBean = context.getBean(SingletonComponent.class);
        singletonBean.operation();

        ClientComponent c = context.getBean(ClientComponent.class);
        c.operation();

        c = (ClientComponent) context.getBean("clientComponent");
        c.operation();
    }

}
