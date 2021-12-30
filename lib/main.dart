import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var switchValue = false;
  String test = 'hello';
  Color _color = Colors.black;
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Flutter Demo',
        theme: ThemeData(
          primaryColor: Colors.blueAccent,
          visualDensity: VisualDensity.adaptivePlatformDensity,
        ),
        darkTheme: ThemeData.light(),
        home: Scaffold(
          appBar: AppBar(title: Text("appbar")),
          bottomNavigationBar: BottomAppBar(
            child: Row(
              children: [
                IconButton(
                    onPressed: () {}, icon: Icon(Icons.icecream_outlined)),
                IconButton(onPressed: () {}, icon: Icon(Icons.menu)),
                IconButton(onPressed: () {}, icon: Icon(Icons.access_alarm))
              ],
            ),
          ),
          body: Center(
            child: ElevatedButton(child: Text('$test'),
            style: ButtonStyle(
              backgroundColor: MaterialStateProperty.all(_color)
            ),
            onPressed: () {
              if (test == 'hello'){
                setState(() {
                  test = 'flutter';
                  _color = Colors.amber;
                });

              } else {
                setState(() {
                  test= 'hello';
                  _color = Colors.blue;
                });
              }

            }),
          ),
        ));
  }
}
