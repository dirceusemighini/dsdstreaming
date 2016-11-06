package controllers;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.actor.ActorPublisher;
import akka.stream.impl.io.InputStreamPublisher;

import akka.actor.ActorRef;
import socket.SocketActor;

/**
 * Created by dirceu on 06/11/16.
 */
public class ActorProducer {
    public static final ActorSystem system = ActorSystem.create("MySystem");
    public static final ActorRef soketActor = system.actorOf(Props.create(SocketActor.class));
//    public ActorPublisher pub = new InputStreamPublisher(actorRef);
}
