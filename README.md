## Simple feed app
Aplikacja łączy sie się do giełdy Coinbase za pomocą websocket API, 
a następnie wyświetla przychodzące dane na consoli. Dane z giełdy to tickery (klasa `TickerMessage`) 
zawierające informacje o zmianie ceny dla danego instrumentu (pole `symbol`).


# Do zrobienia
Należy stworzyć mechanizm, który dla przychodzących danych umożliwi przechowywanie osobno
dla każdego instrumentu ostatniej ceny `bid` i `ask` oraz `timestamp` otrzymanego wpisu z giełdy.
Format przechowywanej wiadomości: `symobl`, `askPrice`, `bidPrice`, `timestamp`.

