package com.Smoke.Signals;

import io.ipfs.multihash.Multihash;

import java.util.ArrayList;
import java.util.HashMap;

class SocialMediaFeed extends Pubsub {
    static HashMap<Long, Post> posts;
    private User yourself;
    private final boolean isPublic;
    private static HashMap<String, Long> publicPages;
    private ArrayList<Pubsub> publicFollows;
    private static String roomName;

    SocialMediaFeed(User yourself, boolean isPublic) throws Exception {
        super(yourself, Account.getSocialMediaRoomName(), true);
        posts = new HashMap<>();
        this.yourself = yourself;
        this.isPublic = isPublic;
        publicPages = new HashMap<>();
        publicFollows = new ArrayList<>();
        roomName = Account.getSocialMediaRoomName();
    }

    /**
     * Posts message to personal chats of all friends and if there is a photo to upload send the hash with it
     * Creates object post
     *
     * @param post The pure text of what needs to be uploaded
     */
    private void postMessage(String post) {
        if (!isPublic)
            for (Peer peer : yourself.getAccount().getPeers().values())
                writeToPubsub(peer.getFullUsername(), post, MessageType.POST);
        else
            writeToPubsub(String.valueOf(yourself.getAccount().getUserId()), post, MessageType.POST);
    }

    /**
     * Posts message to personal chats of all friends and if there is a photo to upload send the hash with it
     * Creates object post
     *
     * @param post        The pure text of what needs to be uploaded
     * @param hashOfImage The multiHash in string form of the image that is posted.
     */
    private void postMessage(String post, Multihash hashOfImage) {
        if (!isPublic)
            for (Peer peer : yourself.getAccount().getPeers().values())
                writeToPubsub(peer.getFullUsername(), post + "#" + hashOfImage.toString(), MessageType.POST);
        else
            writeToPubsub(String.valueOf(yourself.getAccount().getUserId()), post + "#" + hashOfImage.toString(), MessageType.POST);

    }

    /**
     * Uploads a comment to a message
     * Adds a element to the comments arraylist
     * Sends message ending with 5
     *
     * @param comment Pure text of message
     * @param post    the post we are commenting on
     */
    private void commentOnMessage(String comment, Post post) {
        writeToPubsub(post.getMessageId() + "#" + post.getAuthorId() + "#" + comment, MessageType.COMMENT);
    }

    /**
     * Edit message without image. This can either edit a message or remove the image of a previous comment
     *
     * @param newContent new text for the message
     * @param post       the original post that we are editing a comment of
     * @param commentID  the comment we are editing on the post
     */
    private void editComment(String newContent, Post post, Long commentID) {
        writeToPubsub(post.getMessageId() + "#" + post.getAuthorId() + "#" + commentID + newContent, MessageType.EDIT_COMMENT);
    }

    /**
     * Edit comment that will finish with an image in it. This can either add an image or edit the image that is there.
     * The comment does not need to start with an image do use this method.
     *
     * @param newContent new Text that is added
     * @param post       The original post that this message is going to be sent to
     * @param commentID  the id of the comment so we can know what to edit
     * @param image      the String representation of the multihash of the image so that we can get the new image to put in
     */
    private void editComment(String newContent, Post post, long commentID, String image) {
        writeToPubsub(post.getMessageId() + "#" + post.getAuthorId() + "#" +
                commentID + "#" + image + "#" + newContent, MessageType.EDIT_COMMENT_WITH_IMAGE);
    }

    /**
     * Deletes a post already made
     *
     * @param postID the unique id of the post
     */
    private void deletePost(Long postID) {
        writeToPubsub(String.valueOf(postID), MessageType.DELETE_POST);

    }

    /**
     * Deletes a comment already made on a post
     *
     * @param postID    unique id of a post
     * @param commentID unique id of the comment on the post
     */
    private void deleteComment(Long postID, Long commentID) {
        writeToPubsub(postID + "#" + commentID, MessageType.DELETE_COMMENT);
    }

    private void followPublic(String name) {
        for (Peer peer : yourself.getAccount().getPeers().values()) {
            writeToPubsub(peer.getFullUsername(), name, MessageType.GET_PUBLIC_PAGE_NAME);
        }
    }

    void followPublic(long publicID) throws Exception {
        publicFollows.add(new Pubsub(yourself, String.valueOf(publicID), true));
    }
}
