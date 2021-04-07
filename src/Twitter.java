import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Twitter
{

    /*instance variables*/


    /*constants*/

    //maximum length of a username
    private final int USER_NAME_MAX_LENGTH = 15;
    //minimum length of a password
    private final int PASSWORD_MIN_LENGTH = 8;
    //maximum length of a tweet
    private final int TWEET_MAX_LENGTH = 140;
    //maximum length of a name
    private final int NAME_MAX_LENGTH = 50;
    //maximum length of a bio
    private final int BIO_MAX_LENGTH = 160;
    //maximum length of a location input
    private final int LOCATION_MAX_LENGTH = 50;
    //length of a phone number
    private final int PHONE_NUMBER_LENGTH = 10;

    /*file*/

    //file containing saved users
    private File file;
    //file writer & reader
    private ObjectOutputStream writer;
    private ObjectInputStream reader;

    /*status of the application*/

    //arraylist containing every existing user
    private ArrayList<User> existingUsers;
    //arraylist containing every existing tweet
    private ArrayList<Tweet> existingTweets;
    //arraylist containing available commands
    //at any given moment throughout the application
    private ArrayList<String> commands;
    //stores the last input of the user
    private String input;
    //stores the status of the application
    private String status;
    //stores the active user
    private User activeUser;

    /*helper variables*/

    //scanner object
    private Scanner jin;


    /*methods*/

    /*main methods*/

    public static void main(String[] args)
    {
        Twitter app = new Twitter();
        app.loadUsersAndTweets();
        app.mainMenu();
    }

    private void mainMenu()
    {
        //update the status of the application
        commands = new ArrayList<>();
        jin = new Scanner(System.in);
        activeUser = null;
        input = "";
        status = "main menu";
        commands.add("log in");
        commands.add("sign up");
        commands.add("quit the application");
        showStatus();
        printCommands();

        //navigation

        System.out.println("welcome to command line Twitter!\nat any moment throughout the application," +
                " you can see a list of all the commands you can give me.\njust like now! " +
                "just enter the command as an input" +
                " to let me know what I should do next!");

        do
        {
            input = jin.nextLine();

            //check input
            switch (input.toLowerCase().trim())
            {
                //input is valid
                case "log in":
                    logInMenu();
                    return;
                case "sign up":
                    signUpMenu();
                    return;
                case "quit the application":
                    quitMenu();
                    return;

                //input is not valid
                default:
                    break;
            }

            System.out.println("error: invalid input.");

        } while (true);
    }

    private void logInMenu()
    {
        //update the status of the application
        input = "";
        status = "log in menu";
        commands.clear();
        commands.add("main menu");
        commands.add("quit the application");
        showStatus();
        printCommands();

        //the user to become active
        User user;

        //navigation

        //get username
        System.out.println("enter your username.");

        do
        {
            input = jin.nextLine();

            //check if input is a command
            switch (input.toLowerCase().trim())
            {
                //input is a command
                case "main menu":
                    mainMenu();
                    return;
                case "quit the application":
                    quitMenu();
                    return;

                //input is not a command
                default:
                    break;
            }

            //input is not valid
            if (input.equals("") || input.charAt(0) != '@'
                    || input.contains(" ") || input.length() > USER_NAME_MAX_LENGTH)
            {
                System.out.println("error: invalid input.");
            }

            //input is valid
            else
            {
                user = findUser(input);
                //user not found
                if (user == null)
                {
                    System.out.println("error: user not found.");
                }

                //found the user
                else
                {
                    break;
                }
            }
        } while (true);

        //get password
        System.out.println("enter your password.");

        do
        {
            input = jin.nextLine();

            //check if input is a command
            switch (input.toLowerCase().trim())
            {
                //input is a command
                case "main menu":
                    mainMenu();
                    return;
                case "quit the application":
                    quitMenu();
                    return;

                //input is not a command
                default:
                    break;
            }

            //password is correct
            if (input.equals(user.getPassword()))
            {
                break;
            }

            //password is wrong
            System.out.println("error: wrong password.");

        } while (true);

        //make this user the active user
        activeUser = user;
        System.out.println("press enter to go to your home page.");
        jin.nextLine();
        activeUserHomePage();
    }

    private void signUpMenu()
    {
        //update the status of the application
        input = "";
        status = "sign up menu";
        commands.clear();
        commands.add("main menu");
        commands.add("quit the application");
        showStatus();
        printCommands();

        //the username and the password
        //of the user to become active
        String userName;
        String password;

        //navigation

        //get username
        System.out.println("enter your username." +
                "\nit must start with the character '@'." +
                "\nthe maximum allowed length is " + USER_NAME_MAX_LENGTH + " characters." +
                "\nno white space characters are allowed." +
                "\nit must not be taken already.");

        do
        {
            input = jin.nextLine();

            //check if input is a command
            switch (input.toLowerCase().trim())
            {
                //input is a command
                case "main menu":
                    mainMenu();
                    return;
                case "quit the application":
                    quitMenu();
                    return;

                //input is not a command
                default:
                    break;
            }

            //input is not valid
            if (input.equals("") || input.charAt(0) != '@'
                    || input.contains(" ") || input.length() > USER_NAME_MAX_LENGTH)
            {
                System.out.println("error: invalid input.");
            }

            //user not found
            else if (findUser(input) != null)
            {
                System.out.println("error: username already taken.");
            } else
            {
                userName = input;
                break;
            }

        } while (true);

        //get password
        System.out.println("enter your password.\nthe minimum allowed length is " +
                PASSWORD_MIN_LENGTH + " characters.");

        do
        {
            input = jin.nextLine();

            //check if input is a command
            switch (input.toLowerCase().trim())
            {
                //input is a command
                case "main menu":
                    mainMenu();
                    return;
                case "quit the application":
                    quitMenu();
                    return;

                //input is not a command
                default:
                    break;
            }

            //input is not valid
            if (input.length() < PASSWORD_MIN_LENGTH)
            {
                System.out.println("error: invalid input.");
            }

            //password is valid
            else
            {
                System.out.println("confirm password:");
                if (jin.nextLine().equals(input))
                {
                    password = input;
                    break;
                }

                //the conformation password does not match with the original
                else
                {
                    System.out.println("error: the conformation password " +
                            "does not match with the original.");
                }
            }
        } while (true);

        //make a new user and assign it to the active user
        activeUser = new User(userName, password, LocalDateTime.now());
        //add this user to the existing users arraylist and save the changes
        existingUsers.add(activeUser);
        save();
        System.out.println("press enter to go to your home page.");
        jin.nextLine();
        activeUserHomePage();
    }

    private void activeUserHomePage()
    {
        //update the status of the application
        input = "";
        status = "home";
        commands.clear();
        commands.add("new tweet");
        commands.add("(un)retweet a tweet");
        commands.add("(un)like a tweet");
        commands.add("delete a tweet");
        commands.add("view a tweet");
        commands.add("(un)follow a user");
        commands.add("(un)block a user");
        commands.add("view the timeline");
        commands.add("view and edit my profile");
        commands.add("view a user's profile");
        commands.add("log out");
        commands.add("deactivate my account");
        commands.add("quit");
        showStatus();
        printCommands();

        //navigation

        System.out.println("hi " + activeUser.getUsername() + "!");

        activeUser.showMentionedIn();
        save();

        commands.clear();
        commands.add("return home");
        commands.add("quit the application");

        System.out.println("what do you want to do?\nenter the command as" +
                " an input.");

        do
        {
            input = jin.nextLine();

            //check input
            switch (input.trim().toLowerCase())
            {
                case "new tweet":
                    newTweetMenu();
                    return;

                case "(un)retweet a tweet":
                    retweetOrUnretweetATweetMenu();
                    return;

                case "(un)like a tweet":
                    likeOrUnlikeATweetMenu();
                    return;

                case "delete a tweet":
                    deleteATweetMenu();
                    return;

                case "view a tweet":
                    viewATweetMenu();
                    return;

                case "(un)follow a user":
                    followOrUnfollowAUserMenu();
                    return;

                case "(un)block a user":
                    blockOrUnblockAUserMenu();
                    return;

                case "view the timeline":
                    viewTheTimelineMenu();
                    return;

                case "view and edit my profile":
                    viewAndEditMyProfileMenu();
                    return;

                case "view a user's profile":
                    viewAUserProfileMenu();
                    return;

                case "log out":
                    logOutMenu();
                    return;

                case "deactivate my account":
                    deactivateMyAccountMenu();
                    return;

                case "quit":
                    quitMenu();
                    return;

                default:
                    System.out.println("error: invalid input.");
                    break;
            }
        } while (true);
    }

    private void newTweetMenu()
    {
        //update the status of the application
        input = "";
        status = "new tweet menu";
        showStatus();
        printCommands();

        /*helper variables*/

        //this tweet is a reply
        boolean isReply = false;
        //there is a/another mention in this tweet
        boolean hasMention = false;
        //the replied tweet(can be null)
        Tweet inReplyTo = null;
        //arraylist of the mentioned users in this tweet(can be empty)
        ArrayList<User> mentions = new ArrayList<>();


        //navigation

        System.out.println("is this tweet a reply to a tweet?\n" +
                "enter 'yes' for yes or press enter for no.");

        input = jin.nextLine();

        if (input.trim().equalsIgnoreCase("yes"))
        {
            isReply = true;
        }

        if (isReply)
        {
            System.out.println("enter the ID of the tweet you want to reply to.\n" +
                    "enter '-1' to go back.");

            do
            {
                input = jin.nextLine();

                //check if the input is a command
                switch (input.trim().toLowerCase())
                {
                    //the input is a command
                    case "return home":
                        activeUserHomePage();
                        return;
                    case "quit the application":
                        quitMenu();
                        return;

                    //the input is not a command
                    case "-1":
                        newTweetMenu();
                        return;

                    default:
                        break;
                }

                //the input is not valid
                if (!(input.matches("\\d+")))
                {
                    System.out.println("error: " + input + " is not a valid ID for a tweet.");
                }

                //the input is valid
                else
                {
                    inReplyTo = findTweet(input);

                    //tweet not found
                    if (inReplyTo == null)
                    {
                        System.out.println("error: tweet not found.");
                    }

                    //the active user is blocked by the tweet's user
                    else if (inReplyTo.getUser().hasBlocked(activeUser))
                    {
                        System.out.println("error: " + inReplyTo.getUser().getUsername() +
                                " has blocked you, so you cannot reply to any of their tweets.");
                    } else
                    {
                        break;
                    }
                }
            } while (true);
        }

        System.out.println("do you want to mention any users in this tweet?\n" +
                "enter 'yes' for yes or press enter for no.");
        input = jin.nextLine();

        if (input.trim().equalsIgnoreCase("yes"))
        {
            hasMention = true;
        }

        if (hasMention)
        {
            do
            {
                System.out.println("enter the username of the next user you want to mention.\n" +
                        "if there is nobody else, enter '-1'.");
                input = jin.nextLine();

                //check if the input is a command
                switch (input.trim().toLowerCase())
                {
                    //the input is a command
                    case "return home":
                        activeUserHomePage();
                        return;
                    case "quit the application":
                        quitMenu();
                        return;

                    //the input is not a command
                    case "-1":
                        hasMention = false;

                    default:
                        break;
                }

                //there is no other mention
                if (!hasMention)
                {
                    break;
                }

                //there is another mention
                else
                {
                    //the input is not valid
                    if (input.equals("") || input.charAt(0) != '@'
                            || input.contains(" ") || input.length() > USER_NAME_MAX_LENGTH)
                    {
                        System.out.println("error: invalid input.");
                    }

                    //input is valid
                    else
                    {
                        User user = findUser(input);

                        //user not found
                        if (user == null)
                        {
                            System.out.println("error: user not found.");
                        }

                        //the user has blocked the active user
                        else if (user.hasBlocked(activeUser))
                        {
                            System.out.println("error: " + user.getUsername() + " has blocked" +
                                    " you, so you cannot mention them in any of your tweets.");
                        }

                        //this user has already been mentioned
                        else if (mentions.contains(user))
                        {
                            System.out.println("error: you have already mentioned " + input
                                    + ".");
                        } else
                        {
                            mentions.add(user);
                        }
                    }
                }
            } while (true);
        }

        //make a new tweet
        System.out.println("what's on your mind?\nmy attention span isn't " +
                "great, so keep it under " + TWEET_MAX_LENGTH + " characters." +
                " enter '-1' to go back.");

        do
        {
            input = jin.nextLine();

            //check if the input is a command
            switch (input.trim().toLowerCase())
            {
                //the input is a command
                case "return home":
                    activeUserHomePage();
                    return;
                case "quit the application":
                    quitMenu();
                    return;

                //the input is not a command
                case "-1":
                    newTweetMenu();
                    return;

                default:
                    break;
            }

            //input is not valid
            if (input.length() > TWEET_MAX_LENGTH)
            {
                System.out.println("error: what were you saying again?");
            }

            //the input is valid
            else
            {
                //get the text, ID and date of the tweet
                String text = input;
                String ID = generateID();
                LocalDateTime dateOfTweet = LocalDateTime.now();

                //make a new tweet
                Tweet tweet = new Tweet(ID, activeUser, dateOfTweet
                        , mentions, inReplyTo, text);

                //make the tweet
                activeUser.newTweet(tweet);

                //add the tweet to the existing tweets arraylist
                existingTweets.add(tweet);

                //save changes
                save();

                //return to home page
                returnToHomePage();
                return;
            }
        } while (true);
    }

    private void retweetOrUnretweetATweetMenu()
    {
        //update the status of the application
        input = "";
        status = "(un)retweet menu";
        showStatus();
        printCommands();

        System.out.println("enter the ID of the tweet you want to" +
                " (un)retweet.");

        do
        {
            input = jin.nextLine();

            //check if the input is a command
            switch (input.trim().toLowerCase())
            {
                //the input is a command
                case "return home":
                    activeUserHomePage();
                    return;
                case "quit the application":
                    quitMenu();
                    return;

                //the input is not a command
                default:
                    break;
            }

            //input is not valid
            if (!(input.matches("\\d+")))
            {
                System.out.println("error: invalid input.");
            }

            //the input is valid
            else
            {
                Tweet tweet = findTweet(input);

                //tweet not found
                if (tweet == null)
                {
                    System.out.println("error: tweet not found.");
                }

                //the active user is blocked by the tweet's user
                else if (tweet.getUser().hasBlocked(activeUser))
                {
                    System.out.println("error: " + tweet.getUser().getUsername() +
                            " has blocked you, so you cannot reply to any of their tweets.");
                } else
                {
                    //make the (un)retweet
                    activeUser.retweetOrUnretweetATweet(tweet);

                    //save changes
                    save();

                    //return to home page
                    returnToHomePage();
                    return;
                }
            }
        } while (true);
    }

    private void likeOrUnlikeATweetMenu()
    {
        //update the status of the application
        input = "";
        status = "(un)like a tweet menu";
        showStatus();
        printCommands();

        System.out.println("enter the ID of the tweet you want to" +
                " (un)like.");

        do
        {
            input = jin.nextLine();

            //check if the input is a command
            switch (input.trim().toLowerCase())
            {
                //the input is a command
                case "return home":
                    activeUserHomePage();
                    return;
                case "quit the application":
                    quitMenu();
                    return;

                //the input is not a command
                default:
                    break;
            }

            //input is not valid
            if (!(input.matches("\\d+")))
            {
                System.out.println("error: invalid input.");
            }

            //the input is valid
            else
            {
                Tweet tweet = findTweet(input);

                //tweet not found
                if (tweet == null)
                {
                    System.out.println("error: tweet not found.");
                }

                //the active user is blocked by the tweet's user
                else if (tweet.getUser().hasBlocked(activeUser))
                {
                    System.out.println("error: " + tweet.getUser().getUsername() +
                            " has blocked you, so you cannot (un)like any of their tweets.");
                } else
                {
                    //make the (un)like
                    activeUser.likeOrUnlikeATweet(tweet);

                    //save changes
                    save();

                    //return to home page
                    returnToHomePage();
                    return;
                }
            }
        } while (true);
    }

    private void deleteATweetMenu()
    {
        //update the status of the application
        input = "";
        status = "delete a tweet menu";
        showStatus();
        printCommands();

        System.out.println("enter the ID of the tweet you want to" +
                " delete.");

        do
        {
            input = jin.nextLine();

            //check if input is a command
            switch (input.trim().toLowerCase())
            {
                //the input is a command
                case "return home":
                    activeUserHomePage();
                    return;
                case "quit the application":
                    quitMenu();
                    return;

                //the input is not a command
                default:
                    break;
            }

            //input is not valid
            if (!(input.matches("\\d+")))
            {
                System.out.println("error: invalid input.");
            }

            //the input is valid
            else
            {
                Tweet tweet = findTweet(input);

                //tweet not found
                if (tweet == null)
                {
                    System.out.println("error: tweet not found.");
                }

                //the tweet doesn't belong to the active user
                else if (tweet.getUser() != activeUser)
                {
                    System.out.println("error: this tweet is not yours.");
                } else
                {
                    System.out.println("are you sure you want to delete the following tweet?\n" +
                            "this action cannot be undone. enter 'yes' for yes or press enter " +
                            "for no.");
                    System.out.println(tweet);

                    if (jin.nextLine().trim().equalsIgnoreCase("yes"))
                    {
                        //delete the tweet
                        activeUser.deleteATweet(tweet);
                        existingTweets.remove(tweet);

                        //save changes
                        save();
                    }

                    //return to home page
                    returnToHomePage();
                    return;
                }
            }
        } while (true);
    }

    private void viewATweetMenu()
    {
        //update the status of the application
        input = "";
        status = "view a tweet menu";
        showStatus();
        printCommands();

        System.out.println("enter the ID of the tweet you want to" +
                " view. enter '-1' to go back.");

        do
        {
            input = jin.nextLine();

            //check if the input is a command
            switch (input.trim().toLowerCase())
            {
                //the input is a command
                case "return home":
                    activeUserHomePage();
                    return;
                case "quit the application":
                    quitMenu();
                    return;

                //the input is not a command
                default:
                    break;
            }

            //input is not valid
            if (!(input.matches("\\d+")))
            {
                System.out.println("error: invalid input.");
            }

            //the input is valid
            else
            {
                Tweet tweet = findTweet(input);

                //tweet not found
                if (tweet == null)
                {
                    System.out.println("error: tweet not found.");
                }

                //the active user is blocked by the tweet's user
                else if (tweet.getUser().hasBlocked(activeUser))
                {
                    System.out.println("error: " + tweet.getUser().getUsername() +
                            " has blocked you, so you cannot view any of their tweets.");
                } else
                {
                    System.out.println(tweet);
                }
            }
        } while (true);
    }

    private void followOrUnfollowAUserMenu()
    {
        //update the status of the application
        input = "";
        status = "(un)follow a user menu";
        showStatus();
        printCommands();

        System.out.println("enter the username of the user you want to " +
                "(un)follow.");

        do
        {
            input = jin.nextLine();

            //check if the input is a command
            switch (input.trim().toLowerCase())
            {
                //the input is a command
                case "return home":
                    activeUserHomePage();
                    return;
                case "quit the application":
                    quitMenu();
                    return;

                //the input is not a command
                default:
                    break;
            }

            //input is not valid
            if (input.equals("") || input.charAt(0) != '@'
                    || input.contains(" ") || input.length() > USER_NAME_MAX_LENGTH)
            {
                System.out.println("error: invalid input.");
            }

            //input is valid
            else
            {
                User user = findUser(input);

                //user not found
                if (user == null)
                {
                    System.out.println("error: user not found.");
                }

                //the user is the active user
                else if (user == activeUser)
                {
                    System.out.println("error: you cannot follow yourself.");
                }

                //this user has blocked the active user
                else if (user.hasBlocked(activeUser))
                {
                    System.out.println("error: " + input + " has blocked you," +
                            " so you cannot follow them.");
                }

                //the active user has blocked this user
                else if (activeUser.hasBlocked(user))
                {
                    System.out.println("error: you have blocked " + input +
                            ", so you cannot follow them.");
                } else
                {
                    //make the (un)follow
                    activeUser.followOrUnfollowAUser(user);

                    //save changes
                    save();

                    //return to home page
                    returnToHomePage();
                    return;
                }
            }
        } while (true);
    }

    private void blockOrUnblockAUserMenu()
    {
        //update the status of the application
        input = "";
        status = "block/unblock a user menu";
        showStatus();
        printCommands();

        System.out.println("enter the username of the user you want to block/unblock.");

        do
        {
            input = jin.nextLine();

            //check if the input is a command
            switch (input.trim().toLowerCase())
            {
                //the input is a command
                case "return home":
                    activeUserHomePage();
                    return;
                case "quit the application":
                    quitMenu();
                    return;

                //the input is not a command
                default:
                    break;
            }

            //input is not valid
            if (input.equals("") || input.charAt(0) != '@'
                    || input.contains(" ") || input.length() > USER_NAME_MAX_LENGTH)
            {
                System.out.println("error: invalid input.");
            }

            //input is valid
            else
            {
                User user = findUser(input);

                //user not found
                if (user == null)
                {
                    System.out.println("error: user not found.");
                } else if (user == activeUser)
                {
                    System.out.println("error: you cannot block yourself.");
                } else
                {
                    //make the (un)block
                    activeUser.blockOrUnblockAUser(user);

                    //save changes
                    save();

                    //return to home page
                    returnToHomePage();
                    return;
                }
            }
        } while (true);
    }

    private void viewTheTimelineMenu()
    {
        //update the status of the application
        status = "timeline";
        input = "";
        showStatus();

        //view the timeline
        activeUser.viewTheTimeline();

        //return to home page
        returnToHomePage();
    }

    private void viewAndEditMyProfileMenu()
    {
        //update the status of the application and the list of available commands
        status = "profile";
        input = "";
        commands.clear();
        commands.add("change my username");
        commands.add("change my password");
        commands.add("change my name");
        commands.add("change my email");
        commands.add("change my phone number");
        commands.add("change my location");
        commands.add("change my bio");
        commands.add("return home");
        commands.add("quit the application");
        showStatus();
        printCommands();

        System.out.println("this is your profile. you can edit your" +
                " profile by entering a command as an input.");
        System.out.println(activeUser);

        do
        {
            input = jin.nextLine();

            //evaluate the command
            switch (input.trim().toLowerCase())
            {
                case "change my username":
                    System.out.println("enter your new username. " +
                            "\nit must start with the character '@'." +
                            "\nthe maximum allowed length is " + USER_NAME_MAX_LENGTH + " characters." +
                            "\nno white space characters are allowed." +
                            "\nit must not be taken already. enter '-1' to go back.");

                    do
                    {
                        input = jin.nextLine();

                        //the user wants to go back
                        if (input.trim().equals("-1"))
                        {
                            viewAndEditMyProfileMenu();
                            return;
                        }

                        //the user wants to change their username
                        //the input is not valid
                        if (input.equals("") || input.charAt(0) != '@'
                                || input.contains(" ") || input.length() > USER_NAME_MAX_LENGTH)
                        {
                            System.out.println("error: invalid input.");
                        }

                        //the username is not new.
                        else if (activeUser.getUsername().equals(input))
                        {
                            System.out.println("error: " + input + " is your current username.");
                        }

                        //the username is taken
                        else if (findUser(input) != null)
                        {
                            System.out.println("error: " + input + " is taken.");
                        }

                        //the username is available
                        else
                        {
                            //change the user name
                            activeUser.setUserName(input);
                            //save changes
                            save();
                            System.out.println("you have changed your username to " + input + ".");
                            System.out.println("press enter to go back.");
                            jin.nextLine();
                            viewAndEditMyProfileMenu();
                            return;
                        }
                    } while (true);

                case "change my password":
                    System.out.println("enter your new password." +
                            "\nthe minimum allowed length is " + PASSWORD_MIN_LENGTH +
                            " characters. enter '-1' to go back.");

                    do
                    {
                        input = jin.nextLine();

                        //the user wants to go back
                        if (input.trim().equals("-1"))
                        {
                            viewAndEditMyProfileMenu();
                            return;
                        }

                        //the user wants to change their password
                        //the input is not valid
                        if (input.length() < PASSWORD_MIN_LENGTH)
                        {
                            System.out.println("error: invalid input.");
                        }

                        //the password is not new
                        else if (activeUser.getPassword().equals(input))
                        {
                            System.out.println("error: this password is not new.");
                        } else
                        {
                            System.out.println("confirm password:");
                            if (jin.nextLine().equals(input))
                            {
                                //change the password
                                activeUser.setPassword(input);
                                //save changes
                                save();
                                System.out.println("you have changed your password.");
                                System.out.println("press enter to go back.");
                                jin.nextLine();
                                viewAndEditMyProfileMenu();
                                return;
                            }

                            //the conformation password does not match with the original
                            else
                            {
                                System.out.println("error: the conformation password " +
                                        "does not match with the original");
                            }
                        }
                    } while (true);

                case "change my name":
                    System.out.println("enter your new name.\nthe maximum allowed length is " +
                            NAME_MAX_LENGTH + " characters.");

                    do
                    {
                        input = jin.nextLine();

                        //the user wants to go back
                        if (input.trim().equals("-1"))
                        {
                            viewAndEditMyProfileMenu();
                            return;
                        }

                        //the user wants to change their name
                        //the input is not valid
                        if (input.length() > NAME_MAX_LENGTH)
                        {
                            System.out.println("error: invalid input.");
                        }

                        //the name is not new
                        else if (activeUser.getName().equals(input))
                        {
                            System.out.println("error: '" + input + "' is your current name.");
                        } else
                        {
                            //change the name
                            activeUser.setName(input);
                            //save changes
                            save();
                            System.out.println("you have changed your name to " + input +
                                    ".");
                            System.out.println("press enter to go back.");
                            jin.nextLine();
                            viewAndEditMyProfileMenu();
                            return;
                        }
                    } while (true);

                case "change my email":

                    System.out.println("enter your new email.");

                    do
                    {
                        input = jin.nextLine();

                        //the user wants to go back
                        if (input.trim().equals("-1"))
                        {
                            viewAndEditMyProfileMenu();
                            return;
                        }

                        //the user wants to change their email

                        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                                "[a-zA-Z0-9_+&*-]+)*@" +
                                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                                "A-Z]{2,7}$";
                        Pattern pat = Pattern.compile(emailRegex);

                        //the email is not valid
                        if (!(pat.matcher(input).matches()))
                        {
                            System.out.println("error: invalid input.");
                        }

                        //the email is not new
                        else if (activeUser.getEmail().equals(input))
                        {
                            System.out.println("error: " + input + " is your current email.");
                        }

                        //the email is taken
                        else if (findUser(input) != null)
                        {
                            System.out.println("error: this email is taken.");
                        } else
                        {
                            //change the email
                            activeUser.setEmail(input);
                            //save changes
                            save();
                            System.out.println("you have changed your email to " + input +
                                    ".");
                            System.out.println("press enter to go back.");
                            jin.nextLine();
                            viewAndEditMyProfileMenu();
                            return;
                        }
                    } while (true);

                case "change my phone number":

                    System.out.println("enter your new phone number.\n" +
                            "the only allowed characters are digits(Iran) and the length" +
                            "must be " + PHONE_NUMBER_LENGTH + " characters.");

                    do
                    {
                        input = jin.nextLine();

                        //the user wants to go back
                        if (input.trim().equals("-1"))
                        {
                            viewAndEditMyProfileMenu();
                            return;
                        }

                        //the user wants to change their phone number
                        //the phone number is valid
                        if (input.matches("\\d+") && input.length() == PHONE_NUMBER_LENGTH)
                        {
                            //the phone number is not new
                            if (activeUser.getPhoneNumber().equals(input))
                            {
                                System.out.println("error: " + input + " is your current phone number.");
                            }

                            //this phone number is taken
                            if (findUser(input) != null)
                            {
                                System.out.println("error: this phone number is taken.");
                            } else
                            {
                                //change the phone number
                                activeUser.setPhoneNumber(input);
                                //save changes
                                save();
                                System.out.println("you have changed your phone number to " + input +
                                        ".");
                                System.out.println("press enter to go back.");
                                jin.nextLine();
                                viewAndEditMyProfileMenu();
                                return;
                            }
                        }

                        //the phone number is not valid
                        else
                        {
                            System.out.println("error: invalid input.");
                        }
                    } while (true);

                case "change my location":

                    System.out.println("enter your new location.\nthe maximum length allowed " +
                            "is " + LOCATION_MAX_LENGTH + " characters.");

                    do
                    {
                        input = jin.nextLine();

                        //the user wants to go back
                        if (input.trim().equals("-1"))
                        {
                            viewAndEditMyProfileMenu();
                            return;
                        }

                        //the user wants to change their location
                        //the input is invalid
                        if (input.length() > LOCATION_MAX_LENGTH)
                        {
                            System.out.println("error: invalid input.");
                        }

                        //the input is valid
                        else
                        {
                            //change the location
                            activeUser.setLocation(input);
                            //save changes
                            save();
                            System.out.println("you have changed your location to '" + input +
                                    "'.");
                            System.out.println("press enter to go back.");
                            jin.nextLine();
                            viewAndEditMyProfileMenu();
                            return;
                        }
                    } while (true);

                case "change my bio":

                    System.out.println("enter your new bio.\nthe maximum length allowed " +
                            "is " + BIO_MAX_LENGTH + " characters.");

                    do
                    {
                        input = jin.nextLine();

                        //the user wants to go back
                        if (input.trim().equals("-1"))
                        {
                            viewAndEditMyProfileMenu();
                            return;
                        }

                        //the user wants to change their bio
                        //the input is not valid
                        if (input.length() > BIO_MAX_LENGTH)
                        {
                            System.out.println("error: invalid input.");
                        }

                        //the input is valid
                        else
                        {
                            //change the bio
                            activeUser.setBio(input);
                            //save changes
                            save();
                            System.out.println("you have changed your bio to '" + input +
                                    "'.");
                            System.out.println("press enter to go back.");
                            jin.nextLine();
                            viewAndEditMyProfileMenu();
                            return;
                        }
                    } while (true);

                case "return home":
                    activeUserHomePage();
                    return;

                case "quit the application":
                    quitMenu();
                    return;
            }
        } while (true);
    }

    private void viewAUserProfileMenu()
    {
        //update the status of the application
        input = "";
        status = "view a user's profile menu";
        showStatus();
        printCommands();

        System.out.println("enter the username.");

        do
        {
            input = jin.nextLine();

            //check if the input is a command
            switch (input.trim().toLowerCase())
            {
                //the input is a command
                case "return home":
                    activeUserHomePage();
                    return;
                case "quit the application":
                    quitMenu();
                    return;

                //the input is not a command
                default:
                    break;
            }

            //input is not valid
            if (input.equals("") || input.charAt(0) != '@'
                    || input.contains(" ") || input.length() > USER_NAME_MAX_LENGTH)
            {
                System.out.println("error: invalid input.");
            }

            //input is valid
            else
            {
                User user = findUser(input);

                //user not found
                if (user == null)
                {
                    System.out.println("error: user not found.");
                }

                //found the user
                else
                {
                    //this user has blocked the active user
                    if (user.hasBlocked(activeUser))
                    {
                        System.out.println("error: " + input + " has blocked you," +
                                " so you cannot view their profile.");
                    } else
                    {
                        System.out.println(user);

                        //return to home page
                        returnToHomePage();
                        return;
                    }
                }
            }
        } while (true);
    }

    private void logOutMenu()
    {
        System.out.println("are you sure you want to log out?\n" +
                "enter 'yes' for yes or press enter for no.");
        if (jin.nextLine().trim().equalsIgnoreCase("yes"))
        {
            System.out.println("till next time!");
            mainMenu();
        } else
        {
            returnToHomePage();
        }
    }

    private void deactivateMyAccountMenu()
    {
        System.out.println("are you sure you want to deactivate your" +
                " account?\nthis action cannot be undone." +
                " enter 'I want to deactivate my account' for yes" +
                " or press enter to return to your" +
                " home page.");
        if (jin.nextLine().trim().equalsIgnoreCase("I want to deactivate my account"))
        {
            System.out.println("enter your password. enter '-1' to go back.");

            do
            {
                input = jin.nextLine();

                //the user wants to go back
                if (input.trim().equals("-1"))
                {
                    activeUserHomePage();
                    return;
                } else
                {
                    //the password is correct
                    if (activeUser.getPassword().equals(input))
                    {
                        //delete the user
                        existingUsers.remove(activeUser);
                        existingTweets.removeIf(tweet -> tweet.getUser() == activeUser);
                        activeUser.deactivateMyAccount();

                        //save changes
                        save();

                        //return to main menu
                        mainMenu();
                        return;
                    }

                    //the password is not correct
                    else
                    {
                        System.out.println("error: wrong password.");
                    }
                }
            } while (true);
        } else
        {
            returnToHomePage();
        }
    }

    private void quitMenu()
    {
        input = "";
        status = "quit menu";
        showStatus();
        System.out.println("are you sure you want to quit the application?\n" +
                "enter 'yes' for yes or press enter for no.");
        if (jin.nextLine().trim().equalsIgnoreCase("yes"))
        {
            status = "our final moments";
            showStatus();
            System.out.println("I'll be waiting for you...");
            for (int i = 0; i < 50; i++)
            {
                System.out.print("-");
            }
            System.out.println();
            System.exit(1);
        } else
        {
            if (activeUser == null)
            {
                System.out.println("press enter to return to main menu.");
                jin.nextLine();
                mainMenu();
            } else
            {
                System.out.println("press enter to return to your home page.");
                jin.nextLine();
                activeUserHomePage();
            }
        }
    }

    /*helper methods*/

    //a terrible error occurred, terminate the application
    private void errorExit(Exception e)
    {
        System.out.println("error: something went wrong.");
        e.printStackTrace();
        System.out.println("press enter to terminate the application.");
        jin.nextLine();
        System.exit(1);
    }

    private void loadUsersAndTweets()
    {
        existingTweets = new ArrayList<>();
        existingUsers = new ArrayList<>();

        //set up the file and the reader
        try
        {
            file = new File("users.ser");
            //the next line will create the file if it doesn't already exist
            file.createNewFile();
            reader = new ObjectInputStream(new FileInputStream(file));
        } catch (Exception e)
        {
            if (e instanceof EOFException)
            {
                return;
            }
            errorExit(e);
        }


        //read all the users and their tweets from the file to
        //the existingusers and existingtweets arraylists
        while (true)
        {
            try
            {
                System.out.println(reader.readObject().getClass().getName());
                User user = (User) reader.readObject();
                if (user != null)
                {
                    existingUsers.add(user);
                    existingTweets.addAll(user.getTweetsAndReplies());
                }
            } catch (EOFException e)
            {
                Collections.sort(existingTweets);
                break;
            } catch (Exception e)
            {
                errorExit(e);
            }
        }


        //close the reader
        try
        {
            reader.close();
        } catch (IOException e)
        {
            errorExit(e);
        }
    }

    private void save()
    {

        //save every user in the
        //existingusers arraylist to the file
        try
        {
            writer = new ObjectOutputStream(new FileOutputStream(file));
            for (User user : existingUsers)
            {
                writer.writeObject(user);
            }
            writer.close();
        } catch (IOException e)
        {
            errorExit(e);
        }
    }

    //show the status of the application
    private void showStatus()
    {
        //clear the screen(not really)
        for (int i = 0; i < 100; i++)
        {
            System.out.println();
        }

        System.out.print("we're at ");
        //print the status of the application
        if (!(activeUser == null || status.equals("our final moments")))
        {
            System.out.print(activeUser.getUsername() + "'s ");
        }
        System.out.println(status + ".");
        for (int i = 0; i < 50; i++)
        {
            System.out.print("-");
        }
        System.out.println();
    }

    //prints the available commands to the screen
    private void printCommands()
    {
        System.out.println("available commands:");
        for (String command : commands)
        {
            System.out.println(command);
        }
        for (int i = 0; i < 50; i++)
        {
            System.out.print("-");
        }
        System.out.println();
    }

    //return to the active user's home page
    private void returnToHomePage()
    {
        System.out.println("press enter to return to home page.");
        jin.nextLine();
        activeUserHomePage();
    }

    //find the user by their username, phone number or email
    private User findUser(String input)
    {
        //input is a username
        if (input.charAt(0) == '@')
        {
            for (User user : existingUsers)
            {
                if (user.getUsername().equals(input))
                {
                    return user;
                }
            }
            return null;
        }

        //input is a phone number
        else if (input.matches("\\d+"))
        {
            for (User user : existingUsers)
            {
                if (user.getPhoneNumber().equals(input))
                {
                    return user;
                }
            }
            return null;
        }

        //input is an email
        else
        {
            for (User user : existingUsers)
            {
                if (user.getEmail().equals(input))
                {
                    return user;
                }
            }
            return null;
        }
    }

    //find the tweet by its ID
    private Tweet findTweet(String input)
    {
        for (Tweet tweet : existingTweets)
        {
            if (tweet.getID().equals(input))
            {
                return tweet;
            }
        }
        return null;
    }

    //generate an ID for a new tweet
    private String generateID()
    {
        int ID = 0;

        for (Tweet tweet : existingTweets)
        {
            int temp = Integer.parseInt(tweet.getID());
            if (ID <= temp)
            {
                ID = temp + 1;
            }
        }

        return String.valueOf(ID);
    }
}
