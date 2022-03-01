# F29OC Coursework Stub

The specification for this coursework will be issued on Canvas at <https://canvas.hw.ac.uk/courses/5422/pages/concurrency-cw-specification?module_item_id=994983>.

The Constraint Requirements are reproduced below. 

If there is a conflict between these requirements and those defined in the specification document, then the specification document supersedes the reqirements below. 

## 5.3 Constraint Requirements and Penalties

**Note: failure to meet any of the constraint requirements specified in this section will result in a 
penalty being applied and your project may receive a mark of zero (for instance, if we cannot get it 
to compile or you do not have a commit history).**

## 5.3.1 Compilation

CR1.1: Your TenPinManager code must compile with the other F29OC-2021-2022-CW GitLab Stub 
files, using Java SE12 or SE1.8, and under the standard GRID Digital Learning Lab Windows 
computer environment. If it does not compile, then a penalty equivalent to the late penalty 
will be applied and we will attempt simple fixes. If after attempting simple fixes it still does 
not compile your submission will receive a mark of 0.

## 5.3.2 Your Remote GitLab Repository

CR2.1: You must fork this GitLab project into your own private and personal HWU GitLab 
space. 
Your forked project will be your remote repository. 

*If you do 
not fork this project to your own space, then edits that you commit will be broadcast to the 
class and you will not be allowed to use these files in your submission.*

CR2.2: You must not change the name of your GitLab project or any of its files, or add any files to 
the project that you fork from the coursework stub. (Otherwise, we will not be able to 
uniquely find your remote repository.)

CR2.3: Your remote repositories must show evidence of incremental development through their 
GitLab commit and edit histories.

CR2.4: You must not alter your remote GitLab repositories after the deadline. 

## 5.3.3 TenPinManager Class and Interface

CR3.1: Your TenPinManager class must implement our Manager interface and only use the default, 
no-argument, constructor. You must not change this interface. You must not add exception 
‘throws’ to the public methods, otherwise, your code will not compile.

CR3.2: You can include additional private classes written by yourself in TenPinManager.java but 
must not import thread-safe libraries other than the two Extrinsic Monitor classes already 
included.

## 5.3.4 Your Test Software

CR4.1: The source code for your tests should reside in your Tests.java file within your GitLab 
remote repository.

CR4.2: You may use any classes in SE1.8 or SE12 in your Tests.java file.
Note that your test software will not be used for marking but it is required to show evidence 
of incremental development on GitLab. 

## 5.3.5 Thread-safe Classes in TenPinManager

CR5.1: To implement the required concurrent functionality, your TenPinManager must make use of 
the following two Extrinsic Monitor classes:

1. java.util.concurrent.locks.Condition;

2. java.util.concurrent.locks.ReentrantLock;

No other thread-safe or scheduling classes or methods may be used. In particular:
The keyword synchronized, and other classes from the package java.util.concurrent 
should be not be used. 
Thread.Sleep() and any other methods that affect thread scheduling should not be 
used.

“Busy waiting” techniques should not be used. That is, when an instance of your 
TenPinManager is waiting for an event (i.e. a call to booklane() or playerLogin() ) it 
must not consume CPU cycles.
Other non-thread-safe classes from SE1.8 or SE12 may be used, e.g. HashMaps and 
ArrayLists(both of these are unsynchronised and therefore not thread-safe).
