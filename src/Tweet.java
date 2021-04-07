import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Tweet implements Comparable<Tweet>, Serializable
{

    /*instance variables*/

    private String ID;
    private User user;
    private LocalDateTime dateOfTweet;
    //the users mentioned in this tweet
    private ArrayList<User> mentions;
    private String text;
    //the tweets that are in reply to this tweet
    private ArrayList<Tweet> replies;
    private ArrayList<User> retweeters;
    private ArrayList<User> likers;
    private Tweet inReplyTo;
    //this tweet is deleted or not
    private boolean isDeleted;


    /*constructor*/

    public Tweet(String ID, User user, LocalDateTime dateOfTweet,
                 ArrayList<User> mentions, Tweet inReplyTo, String text)
    {
        this.ID = ID;
        this.user = user;
        this.dateOfTweet = dateOfTweet;
        this.mentions = mentions;
        this.inReplyTo = inReplyTo;
        this.text = text;

        replies = new ArrayList<>();
        retweeters = new ArrayList<>();
        likers = new ArrayList<>();

        isDeleted = false;
    }


    /*methods*/

    /*main methods*/

    public void addAReply(Tweet tweet)
    {
        this.replies.add(tweet);
    }

    public void removeAReply(Tweet tweet)
    {
        this.replies.remove(tweet);
    }

    public void removeRetweeter(User user)
    {
        this.retweeters.remove(user);
    }

    public void addRetweeter(User user)
    {
        this.retweeters.add(user);
    }

    public void removeLiker(User user)
    {
        this.likers.remove(user);
    }

    public void addLiker(User user)
    {
        this.likers.add(user);
    }

    public void removeFromMentions(User user)
    {
        this.mentions.remove(user);
    }


    public void delete()
    {
        isDeleted = true;

        //remove this tweet from the mentionedIn list
        //of all the users that had been mentioned in it
        for (User user : mentions)
        {
            user.removeFromMentionedIn(this);
        }

        //remove this tweet from the retweets list
        //of all the users that had retweeted it
        for (User user : retweeters)
        {
            user.removeARetweet(this);
        }

        //remove this tweet from the likedTweets list
        //of all the users that had liked it
        for (User user : likers)
        {
            user.removeALike(this);
        }

        //remove this tweet from the replies arraylist
        //of the tweet that was replied by it
        if (inReplyTo != null)
        {
            inReplyTo.removeAReply(this);
        }
    }

    public String getID()
    {
        return ID;
    }

    @Override
    public int compareTo(Tweet otherTweet)
    {
        return this.dateOfTweet.compareTo(otherTweet.dateOfTweet);
    }

    @Override
    public String toString()
    {
        if (isDeleted)
        {
            return "(this tweet has been deleted.)\n";
        }

        StringBuilder result = new StringBuilder("{\nID: " + ID + ", user: " +
                user.getUsername() + ", date of tweet: "
                + dateOfTweet + ", number of likes: " +
                likers.size() + ", number of replies: " + replies.size());

        if (!(likers.isEmpty()))
        {
            result.append("\nliked by: [");

            for (User user : likers)
            {
                result.append(user.getUsername()).append(", ");
            }

            result.delete(result.length() - 2, result.length());
            result.append("]");
        }

        if (!(mentions.isEmpty()))
        {
            result.append("\nmentioned users: [");

            for (User user : mentions)
            {
                result.append(user.getUsername()).append(", ");
            }

            result.delete(result.length() - 2, result.length());
            result.append("]");
        }

        if (inReplyTo != null)
        {
            result.append("\nin reply to: ").append(inReplyTo.getID());
            result.append(" by ").append(inReplyTo.getUser().getUsername());

            if (inReplyTo.checkDeleted())
            {
                result.append("(this tweet has been deleted)");
            }
        }

        if (!(replies.isEmpty()))
        {
            result.append("\nreplies: [");

            for (Tweet tweet : replies)
            {
                result.append(tweet.getID()).append(", ");
            }

            result.delete(result.length() - 2, result.length());
            result.append("]");
        }

        if (!(retweeters.isEmpty()))
        {
            result.append("\nretweeted by: [");

            for (User user : retweeters)
            {
                result.append(user.getUsername()).append(", ");
            }

            result.delete(result.length() - 2, result.length());

            result.append("]");
        }

        result.append("\ntext: \"").append(text).append("\"\n}\n");
        return result.toString();
    }

    public boolean checkDeleted()
    {
        return isDeleted;
    }

    //getters

    public User getUser()
    {
        return user;
    }

    public Tweet getInReplyTo()
    {
        return inReplyTo;
    }

    public ArrayList<User> getMentions()
    {
        return mentions;
    }
}
