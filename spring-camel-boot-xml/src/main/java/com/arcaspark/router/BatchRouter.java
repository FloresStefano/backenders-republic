/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arcaspark.router;

import com.arcaspark.model.Order;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * A simple Camel REST DSL route with Swagger API documentation.
 */
@Component
public class BatchRouter extends RouteBuilder {

    @Override
    public void configure() {


        // A first route generates some orders and queue them in DB
        from("timer:new-order?delay=1s&period={{example.generateOrderPeriod:20s}}")
                .routeId("generate-order")
                .bean("orderService", "generateOrder")
                .to("jpa:" + Order.class.getSimpleName())
                .log("Inserted new order ${body.id}");

        // A second route polls the DB for new orders and processes them
        from("jpa:" + Order.class.getSimpleName()
                + "?consumer.namedQuery=new-orders"
                + "&consumer.delay={{example.processOrderPeriod:10s}}"
                + "&consumeDelete=false")
                .routeId("process-order")
                .log("Processed order #id ${body.id} with ${body.amount} copies of the «${body.book.description}» book");
    }

}
