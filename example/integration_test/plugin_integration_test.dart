import 'package:flutter_test/flutter_test.dart';

import 'package:flutter_sip2_example/main.dart' as app;

void main() {
  group('end-to-end test', () {
    testWidgets('verify app starts correctly', (tester) async {
      app.main();
      await tester.pumpAndSettle();

      // Verify that the app starts with the correct title
      expect(find.text('Flutter SIP2 Example'), findsOneWidget);
      
      // Verify that the status shows initialization
      expect(find.textContaining('Initializing'), findsOneWidget);
    });
  });
}
