import unittest
import vacancy_nlp
import vacancy_reader

class TestNLPParsing(unittest.TestCase):

    def test_simple_parse(self):
        self.assertEqual(vacancy_nlp.parse(), "parsed!")

if __name__ == '__main__':
    unittest.main()
