# FileEncryption

The simplest file encryption software.

<h1> What it is? </h1>

I want to make it a program that can encrypt any kind of files or data. It is based on the Password-Based Encryption (PBE) using the algorithm of MD5 and Triple DES. Basically, it reads the file, converts the data into bytes, and encrypts the file using randomly generated salt and the password. 

<h1> Current Issues </h1>

I have spent only around 2 hours on it (Of course, it takes like five minutes for some people. I know!) from design to implementation. So, it can only encrypt/decrypt png images. I will add more functionalities to it for sure. 

<h1> How to use it? </h1>

You should run it using console. The syntax of the command is as follows: 
  <ul>
    <li>For Encryption: "java [main] [e] [png_file_path]"</li>
    <li>For Decrypion: "java [main] [e] [encrypted.txt_file_path]"
  </ul>

A few screen shots of how the commands look like are shown below. Before looking at the command, I will discuss what and how the files are generated. When you use the commnd (as shown above) to encrypt a .png file and enter the password, the program will generate a file called "encrypted.txt" in the root directory (where your classes are run). Because it's encrypted, of course you cannot open the txt like a image.
When you use the command to decrypt the "encrypted.txt" file and enter the password, the program will generate a .png file called "output.png". Which is the one that you encrypted. I have tested the program, and it works pretty good. Hopefully, it works all the time.

<h1> Screenshots of the command/ console </h1>

![2019-06-12_231653](https://user-images.githubusercontent.com/45169791/59390117-3d88c700-8d68-11e9-9450-9ce865ef632e.png)

:D 
 
