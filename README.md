# ALP

Aka 'Adaptive Lesson Planner'. 

Why we should use ALP in Open edX research:

Use Bayes' theorem, every time the student answers one question, we would calculate the student prosibilities
(correct or incorrect), to make a 'guess' of what's the percentage of the correctness of his/her next question.

So, if the percentage rate over 92%, we simply hide all the rest of the questions regarding to the same skill.
Otherwise, he/she needs to answer some of them in order to get 92% percentage of correctness.

1. Needs to create a table in Open edX and store all the students' information.

2. Every time student makes an answer, the ALP would calculate the percentage.

3. When the page first finish loading, we get the percentage correctness of the student (per skill), jQuery would automatically 
hide all the related elements.

4. Things need to be solved: should we take a look at the mongodb and change the course structure(sounds risky, all students
would be affected)? If no, students would also notice the course structure would be changed after the page loaded.

