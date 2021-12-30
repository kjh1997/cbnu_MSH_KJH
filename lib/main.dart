import 'package:flutter/material.dart';
import 'imageWidget.dart';
void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'MaterialApp',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: ImageWidgetApp(),
    );
  }
}

class MaterialFlutterApp extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _MaterialFlutterApp();
  }
}

class _MaterialFlutterApp extends State<MaterialFlutterApp> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Material desgin app"),),
      body: Container(
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Icon(Icons.android),
              Text('android')
            ],
          ),
        ),
      ),
      floatingActionButton: FloatingActionButton(child: Icon(Icons.add),
      onPressed: (){
        print('버튼클릭');
      },),
    );
  }
}
