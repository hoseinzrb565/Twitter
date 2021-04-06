import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Tweet implements Comparable<Tweet>, Serializable
{

    /*instance variables*/

    private final String ID;
    private final User user;
    private final LocalDateTime dateOfTweet;
    //the users mentioned in this tweet
    private final ArrayList<User> mentions;
    private final String text;
    //the tweets that are in reply to this tweet
    private final ArrayList<Tweet> replies;
    private final ArrayList<User> retweets;
    private final ArrayList<User> likedBy;
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
        retweets = new ArrayList<>();
        likedBy = new ArrayList<>();

        isDeleted = false;
    }


    /*methods*/

    /*main methods*/

    public void addReply(Tweet tweet)
    {
        this.replies.add(tweet);
    }

    public void removeRetweet(User user)
    {
        retweets.remove(user);
    }

    public void addRetweet(User user)
    {
        retweets.add(user);
    }

    public void removeLike(User user)
    {
        this.likedBy.remove(user);
    }

    public void addLike(User user)
    {
        this.likedBy.add(user);
    }


    public void delete()
    {
        isDeleted = true;
        for (User user : mentions)
        {
            user.removeFromMentionedIn(this);
        }
        inReplyTo = null;
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

        StringBuilder result = new StringBuilder("{ID: " + ID + ", user: " +
                user.getUsername() + ", date of tweet: "
                + dateOfTweet + ", number of likes: " +
                likedBy.size());

        if (!(likedBy.isEmpty()))
        {
            result.append(", liked by: [");

            for (User user : likedBy)
            {
                result.append(user.getUsername()).append(", ");
            }

            result.delete(result.length() - 2, result.length());
            result.append("]");
        }

        if (!(mentions.isEmpty()))
        {
            result.append(", mentioned users: [");

            for (User user : mentions)
            {
                result.append(user.getUsername()).append(", ");
            }

            result.delete(result.length() - 2, result.length());
            result.append("]");
        }

        if (inReplyTo != null)
        {
            result.append(", in reply to: ").append(inReplyTo.getID());
            result.append(" by ").append(inReplyTo.getUser().getUsername());

            if (inReplyTo.checkDeleted())
            {
                result.append("(this tweet has been deleted)");
            }
        }

        if (!(retweets.isEmpty()))
        {
            result.append(", retweeted by: [");

            for (User user : retweets)
            {
                result.append(user.getUsername()).append(", ");
            }

            result.delete(result.length() - 2, result.length());

            result.append("]");
        }

        result.append("\ntext: \"").append(text).append("\"}\n");
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
