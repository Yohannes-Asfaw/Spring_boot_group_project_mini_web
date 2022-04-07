# Qelem-ቀለም
This is a website where students and other users may submit any questions they have and get answers. We believe that this site would serve the educational society by giving an easily accessible means for students to acquire answers to their questions when no one else is available to assist them.

## Features
The following are the primary business features that our website would have:<br /><br /> 
- Our system enables user to have an account, log in to thier account, edit at any time they wanted and delete their account if they insist.<br />
- It provides a way to Post their question or Answer to other question posed by other users, Read questions and Answers posted by them or any other user, Edit the questions and Answer they posted or Delete the questions and Answer they posted.<br />
- It also enable a user to comment on any question, edit his/her comments or delete it.<br />
- Further more our system provides an exam preparaton tests with multiple questions collected from different sources with a proven answers.<br />
- Users will be able to share Educational Material that could possibley address a question raised by other users.<br />
- If a user is reported by other users for posting inappropriate content, they will be assessed by the admin team and removed or suspended depending on the severity of their action via the admins dashboard.
           
The qualities listed above are the main ones that our system will have. We offer an authentication mechanism for the system in order for a user to have an account and login. We will also have a mechanism to provide authorization to change or delete the account that the user created, and for posts made by that user. In addition, admins should be able to suspend or even remove a user's account if the user breaches the terms and conditions of the site, which he/she is supposed to agree to when signing up.

We use the RESTful API to make our system function on a variety of devices. This is an excellent technique to decouple the server from the client and make our work more flexible.
Another significant advantage of the Rest API is that back-end and front-end teams may operate separately without one requiring the other to complete certain duties after both have the intended API. This simplifies the tests we'd do on each unit and paves the path for the best results while doing integration tests.
         
The title we picked for our project is 'Qelem-ቀለም' which is an amharic word that has a contextual equivalence of meaning with "education". We believe that this name accurately represents what we want to undertake.

## Group Memebers
- Hailemariam Arega    UGR/7412/12
- Biruk Tassew         UGR/2004/12
- Yohannes Asfaw       UGR/0327/12
- Petros Beyene        UGR/1308/12
- Nazrawi Demeke       UGR/1209/12

## Settings Needed To Run The Project
- The default user of the system is the admin with 'admin' username and 'admin' password.
- If not photo is uploaded, the system will default back to using the one in the ../static/user-photos/1/ directory.
- The name of the database used is kelem (make sure you have one with such name prior to running the application). As for the password, it is "\n".