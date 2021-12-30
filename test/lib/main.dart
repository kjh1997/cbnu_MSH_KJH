import 'package:flutter/material.dart';

import 'sub/firstpage.dart';
import 'sub/secondpage.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  static const String _title = 'Widget Example';

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: _title,
      home: WidgetApp(),
    );
  }
}

class WidgetApp extends StatefulWidget {
  @override
  _WidgetExampleState createState() => _WidgetExampleState();
}

class _WidgetExampleState extends State<WidgetApp>
    with SingleTickerProviderStateMixin {
  @override
  void dispose() {
    controller!.dispose();
    super.dispose();
  }

  TabController? controller;

  @override
  void initState() {
    super.initState();
    controller = TabController(length: 2, vsync: this);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('TabBar Example'),
      ),
      body: TabBarView(
        children: <Widget>[FirstApp(), SecondApp()],
        controller: controller,
      ),
      bottomNavigationBar: TabBar(
        tabs: <Tab>[
          Tab(
            icon: Icon(Icons.looks_one, color: Colors.blueAccent),
          ),
          Tab(
            icon: Icon(Icons.looks_two, color: Colors.blueAccent),
          )
        ],controller: controller,
      ),
    );
  }00
}
