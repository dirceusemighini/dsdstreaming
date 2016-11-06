package stream;

import akka.actor.ActorRef;
import akka.stream.javadsl.*;
import org.reactivestreams.Publisher;
import play.Logger;


import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


/**
 * Created by dirceu on 05/11/16.
 */
public class TwitterStreamListener {

    private Configuration config;

    ConfigurationBuilder configBuilder = new ConfigurationBuilder();

    TwitterStream twitterStream;

    public TwitterStreamListener() {
        configBuilder.setOAuthConsumerKey("ahbXSE6CHtGMOYCOGkPfkrO5h");
        configBuilder.setOAuthConsumerSecret("SOw6t3tKlj6f1IsZvROMCvvB1h9H04L0y0OTySqXRzpqA11xIn");
        configBuilder.setOAuthAccessToken("86628543-wuxBJVBMLFThiBFGSka4VAqVPdUUVFRjDt808sPnO");
        configBuilder.setOAuthAccessTokenSecret("stdNfRbzEB1SoB39ze24s9mr7u6pTXORE85q8Lkh0CpBu");
        config = configBuilder.build();
        twitterStream = new TwitterStreamFactory(config).getInstance();
    }

    public Source listenAndStream(String searchQuery, ActorRef socketActor, Publisher<Object> publisher) {

        FilterQuery query = new FilterQuery().track(new String[]{searchQuery});

        Logger.info("#start listener for " + searchQuery);

        StatusListener statusListener = new StatusListener() {

            public void onStatus(Status status) {
                Logger.info(status.getUser().getName());
                StringBuilder mensagem = new StringBuilder();
                mensagem.append("<p>");
                mensagem.append(status.getUser().getName());
                mensagem.append(":");
                mensagem.append("&nbsp;");
                mensagem.append(status.getText());
                mensagem.append("</p>");
                socketActor.tell((Object)mensagem.toString() , ActorRef.noSender());
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                Logger.info("onDeletionNotice " + statusDeletionNotice.getStatusId());
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                Logger.info("onTrackLimitationNotice " + numberOfLimitedStatuses);
            }

            public void onException(Exception ex) {
                ex.printStackTrace();
            }

            public void onScrubGeo(long userId, long upToStatusId) {
                Logger.info("onScrubGeo " + userId + " " + upToStatusId);
            }

            public void onStallWarning(StallWarning warning) {
                Logger.info("onStallWarning " + warning.getCode());
            }
        };
        twitterStream.addListener(statusListener);
        twitterStream.filter(query);
        return Source.fromPublisher(publisher);
    }
}
