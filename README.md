Note: You need to have JDK 24, Git and IntelliJ downloaded on your PC


***Running Guidelines for IntelliJ IDEA***

Open IntelliJ IDEA (As Administrator).

From the Welcome Screen, click on “Get from Version Control” (or File > New > Project from Version Control if already inside a project).

In the dialog:

Select Git.

Paste the GitHub repository URL (e.g., https://github.com/username/repo-name.git).

Choose a directory to clone it into.

Click Clone.

IntelliJ will automatically detect the project structure (e.g., Maven/Gradle) and set up the environment.

Now download the mail zip file given, extract it and add the two files in it by File >Project Structure >Libraries > "+" Option > Select two files >Ok > Apply in IntelliJ toolbar

Download javafx zip file and extract it. Now once again go to Run > Edit Configuration > (If empty click on '+' option) Modify Options > Add VM options. A new box will appear. 
Type --module-path "path to javafx lib folder" --add-modules javafx.controls,javafx.fxml there. Remember to replace with actual path.

Next mark the Resources folder as Resources Root by right clicking it in IntelliJ.

Then set up MySQL Workbench and run the SQL script file given.

Now download sql connector file and add the library by adding the jar file in it just like we did with other libraries. 

Finally run and that should run the project.


***Demo Video:*** https://drive.google.com/file/d/1PIrhsjmnRqQujNo0ANpicWOA26Y_kVB-/view?usp=sharing
