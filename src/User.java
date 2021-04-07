import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class User implements Serializable
{

    /*instance variables*/


    //required data

    private String userName;
    private String password;
    private LocalDateTime dateJoined;

    //optional data

    private String name;
    private String phoneNumber;
    private String email;
    private String bio;
    private String location;

    //user activity

    private ArrayList<Tweet> tweets;
    private ArrayList<Tweet> tweetsAndReplies;
    private ArrayList<Tweet> likedTweets;
    private ArrayList<Tweet> retweets;
    //the tweets in which this user is mentioned
    private ArrayList<Tweet> mentionedIn;
    private ArrayList<User> followers;
    private ArrayList<User> following;
    private ArrayList<User> blockedUsers;
    private ArrayList<User> blockedBy;
    //the tweets in the timeline of this user
    private ArrayList<Tweet> timelineTweets;
    //this user is deleted or not
    private boolean isDeleted;


    /*constructor*/

    public User(String userName, String password, LocalDateTime dateJoined)
    {
        this.userName = userName;
        this.password = password;
        this.dateJoined = dateJoined;

        name = "-";
        phoneNumber = "-";
        email = "-";
        bio = "-";
        location = "-";

        tweets = new ArrayList<>();
        tweetsAndReplies = new ArrayList<>();
        likedTweets = new ArrayList<>();
        retweets = new ArrayList<>();
        mentionedIn = new ArrayList<>();
        followers = new ArrayList<>();
        following = new ArrayList<>();
        blockedUsers = new ArrayList<>();
        blockedBy = new ArrayList<>();


        isDeleted = false;
    }


    /*methods*/

    /*main methods*/

    public void showMentionedIn()
    {
        if (this.mentionedIn.isEmpty())
        {
            return;
        }

        System.out.println("I have something to tell you!");
        System.out.println("you have been mentioned in the following tweet(s).");
        int i = 1;

        for (Tweet tweet : mentionedIn)
        {
            System.out.println(i + ")");
            System.out.println(tweet);
            i++;
        }

        mentionedIn.clear();
    }

    public void newTweet(Tweet tweet)
    {
        //add the tweet to its user's 'tweets and replies' array list
        this.tweetsAndReplies.add(tweet);

        //the tweet is a reply
        if (tweet.getInReplyTo() != null)
        {
            //add the tweet to the replied tweet's 'replies' array list
            tweet.getInReplyTo().addAReply(tweet);
        }

        //this tweet is not a reply
        else
        {
            //add the tweet to this user's 'tweets' array list
            this.tweets.add(tweet);
        }

        //some users are mentioned in this tweet
        if (!(tweet.getMentions().isEmpty()))
        {
            //add the tweet to all the mentioned users' 'mentionedIn' array list
            //so the next time they become active I can tell them
            for (User mentionedUser : tweet.getMentions())
            {
                mentionedUser.addToMentionedIn(tweet);
            }
        }

        //display the tweet
        System.out.println("you have made the following tweet.");
        System.out.println(tweet);
    }

    public void retweetOrUnretweetATweet(Tweet tweet)
    {
        //the user wants to unretweet this tweet
        if (this.retweets.contains(tweet))
        {
            this.retweets.remove(tweet);
            tweet.removeRetweeter(this);
            System.out.println("you have unretweeted the following tweet.");
            System.out.println(tweet);
        }

        //the user wants to retweet the tweet
        else
        {
            this.retweets.add(tweet);
            tweet.addRetweeter(this);
            System.out.println("you have retweeted the following tweet.");
            System.out.println(tweet);
        }
    }

    public void likeOrUnlikeATweet(Tweet tweet)
    {
        //the user wants to unlike this tweet
        if (this.likedTweets.contains(tweet))
        {
            //remove this tweet from the liked tweets arraylist
            this.likedTweets.remove(tweet);
            //remove the user from this tweet's likes array list
            tweet.removeLiker(this);
            System.out.println("you have unliked the following tweet.");
        }

        //the user wants to like the tweet
        else
        {
            //add this tweet to the liked tweets arraylist
            this.likedTweets.add(tweet);
            //add the user to this tweet's likes array list
            tweet.addLiker(this);
            System.out.println("you have liked the following tweet.");
        }
        System.out.println(tweet);
    }

    public void deleteATweet(Tweet tweet)
    {
        //remove the tweet from all of the user's arraylists
        this.tweets.remove(tweet);
        this.tweetsAndReplies.remove(tweet);
        this.likedTweets.remove(tweet);
        this.mentionedIn.remove(tweet);
        this.retweets.remove(tweet);

        //delete the tweet
        tweet.delete();

        System.out.println("tweet deleted.");
    }

    public void followOrUnfollowAUser(User user)
    {
        System.out.print("you have ");

        //the active user wants to unfollow this user
        if (this.following.contains(user))
        {
            //remove this user from the active user's following list
            this.following.remove(user);
            //remove the active user from this user's followers list
            user.removeFollower(this);

            System.out.print("un");
        }

        //the active user wants to follow this user
        else
        {
            //add this user to the active user's following list
            this.following.add(user);
            //add the active user to this user's followers list
            user.addFollower(this);
        }

        System.out.println("followed " + user.getUsername() + ".");
    }

    public void blockOrUnblockAUser(User user)
    {
        //the active user wants to unblock this user
        if (this.blockedUsers.contains(user))
        {
            //remove this user from the active user's blocked users list
            this.blockedUsers.remove(user);
            //remove the active user from this user's blockedby list
            user.removeFromBlockedBy(this);
            System.out.println("you have unblocked " + user.getUsername() + ".");
        }

        //the active user wants to block this user
        else
        {
            //add this user to the active user's blocked list
            this.blockedUsers.add(user);
            //add the active user to this user's blockedby list
            user.addToBlockedBy(this);
            //remove this two users from each other's following and follower list
            this.following.remove(user);
            this.followers.remove(user);
            user.removeFollowing(this);
            user.removeFollower(this);
            System.out.println("you have blocked " + user.getUsername() + ".");
        }
    }

    public void viewTheTimeline()
    {
        fillTimeline();
        displayTimeline();
    }

    public void deactivateMyAccount()
    {
        isDeleted = true;

        //remove the user from all of their followers' followings list
        for (User follower : followers)
        {
            follower.removeFollowing(this);
        }

        //remove the user from all of their followings' followers list
        for (User following : following)
        {
            following.removeFollower(this);
        }

        //delete all of this user's tweets
        for (Tweet tweet : tweetsAndReplies)
        {
            tweet.delete();
        }

        //remove this user from the tweets that
        //had mentioned him/her
        for (Tweet tweet : mentionedIn)
        {
            tweet.removeFromMentions(this);
        }

        //remove this user from the likers list of the tweets
        //that he/she had liked
        for (Tweet tweet : likedTweets)
        {
            tweet.removeLiker(this);
        }

        //remove this user from the retweeters list of the tweets
        //that he/she has retweeted
        for (Tweet tweet : retweets)
        {
            tweet.removeRetweeter(this);
        }

        //remove this user from the blockedusers list
        //of the users in his/her blockedby list
        for (User user : blockedBy)
        {
            user.removeFromBlockedBy(this);
        }

        //clear every one of this user's arraylists
        tweets.clear();
        tweetsAndReplies.clear();
        likedTweets.clear();
        retweets.clear();
        mentionedIn.clear();
        followers.clear();
        following.clear();
        blockedUsers.clear();
        blockedBy.clear();
    }

    /*helper methods*/

    public void addToMentionedIn(Tweet tweet)
    {
        this.mentionedIn.add(tweet);
    }

    public void removeFromMentionedIn(Tweet tweet)
    {
        this.mentionedIn.remove(tweet);
    }

    public boolean hasBlocked(User user)
    {
        return this.blockedUsers.contains(user);
    }

    private void fillTimeline()
    {
        timelineTweets = new ArrayList<>();

        //add all of this user's tweets to the timeline
        timelineTweets.addAll(this.tweetsAndReplies);

        //add all of the tweets of the user's following list
        //to the timeline
        for (User user : following)
        {
            timelineTweets.addAll(user.getTweetsAndReplies());
        }

        //sort the timelinetweets arraylist by the date of the tweet
        Collections.sort(timelineTweets);
    }

    private void displayTimeline()
    {
        System.out.println("this is your timeline.");
        int i = 1;
        //display all the tweets in the timeline
        for (Tweet tweet : timelineTweets)
        {
            System.out.println(i + ")");
            System.out.println(tweet);
            i++;
        }
    }

    public void removeFollower(User user)
    {
        this.followers.remove(user);
    }

    public void removeFollowing(User user)
    {
        this.following.remove(user);
    }

    public void addFollower(User user)
    {
        this.followers.add(user);
    }

    public void removeALike(Tweet tweet)
    {
        this.likedTweets.remove(tweet);
    }

    public void removeARetweet(Tweet tweet)
    {
        this.retweets.remove(tweet);
    }

    public void addToBlockedBy(User user)
    {
        this.blockedBy.add(user);
    }

    public void removeFromBlockedBy(User user)
    {
        this.blockedBy.remove(user);
    }

    //getters & setters

    public ArrayList<Tweet> getTweetsAndReplies()
    {
        return tweetsAndReplies;
    }

    public String getUsername()
    {
        return userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

    @Override
    public String toString()
    {
        int i;
        if (isDeleted)
        {
            return "this user has deactivated their account.";
        }

        //display the overall information
        StringBuilder result = new StringBuilder("user name: " + userName
                + "\nname: " + name
                + "\nbio: " + bio
                + "\nemail: " + email
                + "\nphone number: " + phoneNumber
                + "\ndate joined: " + dateJoined
                + "\nlocation: " + location
                + "\nnumber of followers: " + followers.size()
                + "\nnumber of followings: " + following.size()
                + "\nnumber of blocked users: " + blockedUsers.size()
                + "\nnumber of total tweets: " + tweetsAndReplies.size());

        //display every tweet
        result.append("\n\ntweets: ");

        if (tweets.isEmpty())
        {
            result.append("[]");
        } else
        {
            result.append("\n\n");
            i = 1;
            for (Tweet tweet : tweets)
            {
                if (retweets.contains(tweet) && tweet.getUser() != this)
                {
                    result.append("(").append(userName).append(" Retweeted) ");
                }
                result.append(i).append(")\n").append(tweet).append("\n");
                i++;
            }
        }

        //display every tweet and reply
        result.append("\ntweets and replies: ");

        if (tweetsAndReplies.isEmpty())
        {
            result.append("[]");
        } else
        {
            result.append("\n\n");
            i = 1;
            for (Tweet tweet : tweetsAndReplies)
            {
                result.append(i).append(")\n").append(tweet).append("\n");
                i++;
            }
        }


        //display every follower
        result.append("\nfollowers: ");

        if (followers.isEmpty())
        {
            result.append("[]");
        } else
        {
            result.append("\n\n");
            i = 1;
            for (User user : followers)
            {
                result.append(i).append(")\n").append(user.getUsername()).append("\n");
                i++;
            }
        }

        //display every following
        result.append("\nfollowing: ");

        if (following.isEmpty())
        {
            result.append("[]");
        } else
        {
            result.append("\n\n");
            i = 1;
            for (User user : following)
            {
                result.append(i).append(")\n").append(user.getUsername()).append("\n");
                i++;
            }
        }

        //display every blocked user
        result.append("\nblocked users: ");

        if (blockedUsers.isEmpty())
        {
            result.append("[]");
        } else
        {
            result.append("\n\n");
            i = 1;
            for (User user : blockedUsers)
            {
                result.append(i).append(")\n").append(user.getUsername()).append("\n");
                i++;
            }
        }

        return result.append("\n").toString();
    }
}

