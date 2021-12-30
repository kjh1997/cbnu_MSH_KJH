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
          appBar: AppBar( title: Text("appbar")),
          bottomNavigationBar: BottomAppBar(
              child: Row(

                children: [
                IconButton(onPressed: (){}, icon: Icon(Icons.icecream_outlined)),
                IconButton(onPressed: (){}, icon: Icon(Icons.menu)),
                  IconButton(onPressed: (){}, icon: Icon(Icons.access_alarm))
                ],
              ),),
          body: Center(
            child: Switch(
                value: switchValue,
                onChanged: (value) {
                  setState(() {
                    print(value);
                    switchValue = value;
                  });

                }),
          ),
        ));
  }
}
