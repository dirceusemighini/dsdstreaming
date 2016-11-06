package controllers;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.japi.Pair;
import akka.stream.Materializer;
import akka.stream.OverflowStrategy;
import akka.stream.impl.ActorMaterializerImpl;
import akka.stream.javadsl.AsPublisher;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import org.reactivestreams.Publisher;
import play.Logger;
import play.libs.EventSource;

import stream.TwitterStreamListener;

import java.util.ArrayList;
import java.util.List;

import play.mvc.*;

import javax.inject.Singleton;

/**
 * Created by dirceu on 05/11/16.
 */
@Singleton
public class ApplicationController extends Controller {

    static TwitterStreamListener twitterListener = new TwitterStreamListener();
    private static ActorSystem system = ActorSystem.create("mixedTweets");

    private static Materializer mat = ActorMaterializerImpl.create(system);
    static final Pair<ActorRef, Publisher<Object>> ti = Source.actorRef(100, OverflowStrategy.fail()).toMat(Sink.asPublisher(AsPublisher.WITHOUT_FANOUT), Keep.both()).run(mat);//.actorPublisher(publisher);
    static ActorRef socketActor = ti.first();
    static Publisher<Object> publisher = ti.second();

    public play.mvc.Result stream(String queries) {

        Source<String, ?> source = twitterListener.listenAndStream(queries, socketActor, publisher);
        final Source<EventSource.Event, ?> eventSource = source.map(eve ->
        {
            return EventSource.Event.event(eve);
        });
        return ok().chunked(eventSource.via(EventSource.flow())).as(Http.MimeTypes.HTML);
    }

    public play.mvc.Result liveTweets(List<String> query) {
        return stream((query.get(0)));
    }

    public play.mvc.Result index() {
        //default search
        List<String> list = new ArrayList();
        list.add("java");
        list.add("ruby");
        return redirect(routes.ApplicationController.liveTweets(list));
    }
}
