// import 'package:flutter/material.dart';
// import 'package:firebase_database/firebase_database.dart';
// import 'memo.dart';
// import 'memoAdd.dart';
//
// class MemoPage extends StatefulWidget{
//   @override
//   State<StatefulWidget> createState() => _MemoPage();
//
// }
// class _MemoPage extends State<MemoPage>{
//   FirebaseDatabase? _database;
//   DatabaseReference? reference;
//   String _databaseURL = "https://whiteman-prj-default-rtdb.firebaseio.com/";
//   List<Memo> memos = new List.empty(growable: true);
//
//   @override
//   void initState(){
//     super.initState();
//     _database = FirebaseDatabase(databaseURL: _databaseURL);
//     reference = _database!.reference().child('memo');
//     reference!.onChildAdded.listen((event) {
//      print(event.snapshot.value.toString());
//      setState(() {
//        memos.add(Memo.fromSnapshot(event.snapshot));
//      });
//    });
//   }
// }