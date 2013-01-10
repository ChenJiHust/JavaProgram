package chenji.interview;

/**
//Description: You are promised an input stream consisting of English letters and punctuations. It is guaranteed that
//the words (sequence of consecutive letters) are delimited by one and only one punctuation; that
//the stream will begin with a word; that
//the words will be at least one letter long; and that
//a full stop (.) appears after, and only after, the last word.
//For example, what,is,the;meaning,of:life. is such a stream with six words. Your task is to reverse the letters in every other word while leaving punctuations intact, producing e.g. "what,si,the;gninaem,of:efil.", while observing the following restrictions:
//Only I/O allowed is reading or writing one character at a time, which means: no reading in a string, no peeking ahead, no pushing characters back into the stream, and no storing characters in a global variable for later use;
//You are not to explicitly save characters in a collection data structure, such as arrays, strings, hash tables, etc, for later reversal;
//You are allowed to use recursions, closures, continuations, threads, coroutines, etc., even if their use implies the storage of multiple characters.
*/

public class OddWord {
	interface CharHandler {
		CharHandler handle(char c) throws Exception;
	}

	final CharHandler fwd = new CharHandler() {
		public CharHandler handle(char c) {
			System.out.print(c);
			return (Character.isLetter(c) ? fwd : rev);
		}
	};

	class Reverser extends Thread implements CharHandler {
		Reverser() {
			setDaemon(true);
			start();
		}

		private Character ch; // For inter-thread comms

		private char recur() throws Exception {
			notify();
			while (ch == null)
				wait();
			char c = ch, ret = c;
			ch = null;
			if (Character.isLetter(c)) {
				ret = recur();
				System.out.print(c);
			}
			return ret;
		}

		public synchronized void run() {
			try {
				while (true) {
					System.out.print(recur());
					notify();
				}
			} catch (Exception e) {
			}
		}

		public synchronized CharHandler handle(char c) throws Exception {
			while (ch != null)
				wait();
			ch = c;
			notify();
			while (ch != null)
				wait();
			return (Character.isLetter(c) ? rev : fwd);
		}
	}

	final CharHandler rev = new Reverser();

	public void loop() throws Exception {
		CharHandler handler = fwd;
		int c;
		while ((c = System.in.read()) >= 0) {
			handler = handler.handle((char) c);
		}
	}

	public static void main(String[] args) throws Exception {
		new OddWord().loop();
	}
}
