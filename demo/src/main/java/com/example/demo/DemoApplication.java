package com.example.demo;

// import classses.Author;
// import classses.Book;
// import classses.BookStatistics;
// import classses.GenerateToC;
// import classses.Image;
// import classses.ImageProxy;
// import classses.Paragraph;
// import classses.RenderContentVisitor;
// import classses.Section;
// import classses.Table;

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
