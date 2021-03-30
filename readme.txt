////////////////////////////////////////////////////////////////////////////////////

Amiram Abergel - Amiram.Abergel@post.idc.ac.il - 204147219

Tom Hatuel - Tom.Hatuel@post.idc.ac.il - 305327272

/////////////////////////////////////////////////////////////////////////////////////

Our Project Structure:

SinkholeServer.java - The Recursive server's entry point To Get/Send Packets (Include Main func).

DEF.java - A Class Containt All Unchanged Known Values.

Flags.java - A Class That Translates The Values In The DNS Query Bytes.

DNSQuery.java - A Class That Represents DNS Query Bytes And Anlayze Them.

BlockList.java - A Class Stores a HashSet For Blocked Domain.

Question.java - A Class That Save A DNS Question Record.

ResourceRecord.java - A Class That Save ANSWER AND AUTHIRITY Of A DNS.

Helper.java - A Class That Have All Necessary  Functions For BitWise Operation On The Dns Query.

/////////////////////////////////////////////////////////////////////////////////////

Thank you.

/////////////////////////////////////////////////////////////////////////////////////

javac -d out/ -Xlint src/il/ac/idc/cs/sinkhole/*.java --release 11