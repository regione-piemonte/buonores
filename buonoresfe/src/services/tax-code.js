/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export const GENDERS = { MALE: "M", FEMALE: "F" };
export const VOWELS = ["A", "E", "I", "O", "U"];

export const TEMPORARY_LENGTH = 11;
export const LENGTH = 16;
export const SURNAME_INDEX_START = 0;
export const SURNAME_INDEX_END = 2;
export const NAME_INDEX_START = 3;
export const NAME_INDEX_END = 5;
export const YEAR_INDEX_START = 6;
export const YEAR_INDEX_END = 7;
export const MONTH_INDEX = 8;
export const DAY_AND_GENDER_INDEX_START = 9;
export const DAY_AND_GENDER_INDEX_END = 10;
export const BIRTH_PLACE_INDEX_START = 11;
export const BIRTH_PLACE_INDEX_END = 14;
export const CIN_INDEX = 15;
export const REGEX = /^[A-Za-z]{6}[0-9]{2}[A-Za-z][0-9]{2}[A-Za-z][0-9]{3}[A-Za-z]$/;
// Ogni lettera corrisponde ad un numero di un mese. Es: A = 0 Gennaio, B = 1 Febbraio
export const MONTH_MAP = "ABCDEHLMPRST".split("");
// Ogni lettera corrisponde ad un resto delle divisione per 26. es A = 0, Z = 25
export const CIN_MOD_MAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
// Ogni lettera corrisponde alla cifra da sostituire in caso di omocodia. es 0 -> L, 5 -> R
// @TODO gestire l'omocodia.
export const OMOCODIA_MAP = "LMNPQRSTUV".split("");
// Numero massimo di cifre presenti nel codice fiscale:
//  2 (Anno nascita) + 2 (Giorno nascita + sesso) + 3 (Codice luogo di nascita)
export const OMOCODIA_MAX_LEVEL = 7;
// Prendiamo i valori associati ad ogni carattere del codice fiscale
// in base alla loro posizione pari (even) o dispari (odd)
export const CIN_CHAR_MAP = {
  "0": { odd: 1, even: 0 },
  "1": { odd: 0, even: 1 },
  "2": { odd: 5, even: 2 },
  "3": { odd: 7, even: 3 },
  "4": { odd: 9, even: 4 },
  "5": { odd: 13, even: 5 },
  "6": { odd: 15, even: 6 },
  "7": { odd: 17, even: 7 },
  "8": { odd: 19, even: 8 },
  "9": { odd: 21, even: 9 },
  A: { odd: 1, even: 0 },
  B: { odd: 0, even: 1 },
  C: { odd: 5, even: 2 },
  D: { odd: 7, even: 3 },
  E: { odd: 9, even: 4 },
  F: { odd: 13, even: 5 },
  G: { odd: 15, even: 6 },
  H: { odd: 17, even: 7 },
  I: { odd: 19, even: 8 },
  J: { odd: 21, even: 9 },
  K: { odd: 2, even: 10 },
  L: { odd: 4, even: 11 },
  M: { odd: 18, even: 12 },
  N: { odd: 20, even: 13 },
  O: { odd: 11, even: 14 },
  P: { odd: 3, even: 15 },
  Q: { odd: 6, even: 16 },
  R: { odd: 8, even: 17 },
  S: { odd: 12, even: 18 },
  T: { odd: 14, even: 19 },
  U: { odd: 16, even: 20 },
  V: { odd: 10, even: 21 },
  W: { odd: 22, even: 22 },
  X: { odd: 25, even: 23 },
  Y: { odd: 24, even: 24 },
  Z: { odd: 23, even: 25 }
};

const _isAlpha = char => {
  return /[a-zA-Z]/.test(char);
};

const _isDigit = char => {
  return /\d/.test(char);
};

const isVowel = char => {
  return VOWELS.indexOf(char.toUpperCase()) !== -1;
};

const isConsonant = char => {
  return _isAlpha(char) && !isVowel(char);
};

const getVowels = string => {
  let chars = string.split("");
  return chars.filter(isVowel);
};

const getConsonants = string => {
  let chars = string.split("");
  return chars.filter(isConsonant);
};

// GENERAZIONE DI UN CODICE FISCALE
// ---------------------------------------------------------------------------------------------------------------------
const _addVowelsIfNeeded = (result, string) => {
  if (result.length >= 3) return result;
  let vowels = getVowels(string);
  return result.concat(vowels);
};

const _addXIfNeeded = result => {
  if (result.length >= 3) return result;
  return result.concat(["X", "X", "X"]);
};

const _fillIfNeeded = (result, string) => {
  result = _addVowelsIfNeeded(result, string);
  result = _addXIfNeeded(result);
  return result;
};

export const generateSurnamePart = surname => {
  surname = surname.toUpperCase();
  let result = getConsonants(surname);
  result = _fillIfNeeded(result, surname);
  return result
    .join("")
    .substring(0, 3)
    .toUpperCase();
};

export const generateNamePart = name => {
  name = name.toUpperCase();
  let result = getConsonants(name);
  if (result.length >= 4) result.splice(1, 1);
  result = _fillIfNeeded(result, name);
  return result
    .join("")
    .substring(0, 3)
    .toUpperCase();
};

export const generateBirthYearPart = year => {
  let yearString = String(year);
  return yearString.substring(yearString.length - 2);
};

export const generateBirthMonthPart = monthNumber => {
  return MONTH_MAP[Number(monthNumber)];
};

export const generateBirthDayAndGenderPart = (day, gender) => {
  let result = Number(day);
  if (gender === GENDERS.FEMALE) result += 40;
  return String(result);
};

export const generatePartialTaxCode = person => {
  let result =
    generateSurnamePart(person.surname) +
    generateNamePart(person.name) +
    generateBirthYearPart(person.birthYear) +
    generateBirthMonthPart(person.birthMonth) +
    generateBirthDayAndGenderPart(person.birthDay, person.gender) +
    person.birthPlaceCode.toUpperCase();
  return result;
};

export const generateCin = (partialTaxCode = "") => {
  partialTaxCode = partialTaxCode.toUpperCase();
  let partialTaxCodeChars = partialTaxCode.split("");
  let odd = partialTaxCodeChars.filter(
    (char, index) => index <= 14 && (index + 1) % 2 === 1
  );
  let even = partialTaxCodeChars.filter(
    (char, index) => index <= 14 && (index + 1) % 2 === 0
  );
  let oddSum = odd.reduce((sum, char) => sum + CIN_CHAR_MAP[char].odd, 0);
  let evenSum = even.reduce((sum, char) => sum + CIN_CHAR_MAP[char].even, 0);
  let mod = (evenSum + oddSum) % 26;
  return CIN_MOD_MAP[mod];
};

export const generateTaxCode = person => {
  let partialTaxCode = generatePartialTaxCode(person);
  let cin = generateCin(partialTaxCode);
  return partialTaxCode + cin;
};

export const generateAllParts = person => {
  return {
    surname: generateSurnamePart(person.surname),
    name: generateNamePart(person.name),
    birthYear: generateBirthYearPart(person.birthYear),
    birthMonth: generateBirthMonthPart(person.birthMonth),
    birthDayAndGender: generateBirthDayAndGenderPart(
      person.birthDay,
      person.gender
    ),
    partialTaxCode: generatePartialTaxCode(person),
    cin: generateCin(generatePartialTaxCode(person)),
    taxCode: generateTaxCode(person)
  };
};

// PARSING DI UN CODICE FISCALE
// ---------------------------------------------------------------------------------------------------------------------
export const extractSurnamePart = (taxCode = "") => {
  taxCode = taxCode.toUpperCase();
  return taxCode.substring(SURNAME_INDEX_START, SURNAME_INDEX_END + 1);
};

export const extractNamePart = (taxCode = "") => {
  taxCode = taxCode.toUpperCase();
  return taxCode.substring(NAME_INDEX_START, NAME_INDEX_END + 1);
};

export const extractBirthYearPart = (taxCode = "") => {
  taxCode = taxCode.toUpperCase();
  return taxCode.substring(YEAR_INDEX_START, YEAR_INDEX_END + 1);
};

export const extractBirthMonthPart = (taxCode = "") => {
  taxCode = taxCode.toUpperCase();
  return taxCode.charAt(MONTH_INDEX);
};

export const extractBirthDayAndGenderPart = (taxCode = "") => {
  taxCode = taxCode.toUpperCase();
  return taxCode.substring(
    DAY_AND_GENDER_INDEX_START,
    DAY_AND_GENDER_INDEX_END + 1
  );
};

export const extractBirthPlaceCodePart = (taxCode = "") => {
  taxCode = taxCode.toUpperCase();
  return taxCode.substring(BIRTH_PLACE_INDEX_START, BIRTH_PLACE_INDEX_END + 1);
};

export const extractPartialTaxCode = (taxCode = "") => {
  taxCode = taxCode.toUpperCase();
  let chars = taxCode.split("");
  chars.splice(CIN_INDEX, 1);
  return chars.join("");
};

export const extractCinPart = (taxCode = "") => {
  taxCode = taxCode.toUpperCase();
  return taxCode.charAt(CIN_INDEX);
};

export const extractAllParts = (taxCode = "") => {
  return {
    surname: extractSurnamePart(taxCode),
    name: extractNamePart(taxCode),
    birthYear: extractBirthYearPart(taxCode),
    birthMonth: extractBirthMonthPart(taxCode),
    birthDayAndGender: extractBirthDayAndGenderPart(taxCode),
    birthPlaceCode: extractBirthPlaceCodePart(taxCode),
    partialTaxCode: extractPartialTaxCode(taxCode),
    cin: extractCinPart(taxCode),
    gender: getGender(taxCode),
    day: getDay(taxCode)
  };
};

export const getGender = (taxCode = "") => {
  let dayAndGenderPart = extractBirthDayAndGenderPart(taxCode);
  let number = Number(dayAndGenderPart);
  if (isNaN(number)) return undefined;
  return number > 40 ? GENDERS.FEMALE : GENDERS.MALE;
};

export const getDay = (taxCode = "") => {
  let gender = getGender(taxCode);
  let result = Number(extractBirthDayAndGenderPart(taxCode));
  if (gender === GENDERS.FEMALE) result -= 40;
  return result;
};

// OMOCODIA
// ---------------------------------------------------------------------------------------------------------------------
const _getOneLevelAlternativeTaxCodes = (taxCode = "") => {
  let result = [];
  // Il carattere di controllo deve essere ricalcolato una volta che viene sostituita una cifra.
  let partialTaxCode = extractPartialTaxCode(taxCode);
  let chars = partialTaxCode.split("");

  chars.forEach((char, index) => {
    if (!_isDigit(char)) return;
    let omocodiaChar = OMOCODIA_MAP[Number(char)];
    let alternative = partialTaxCode.split("");
    alternative[index] = omocodiaChar;
    alternative = alternative.join("");
    let newCin = generateCin(alternative);
    result.unshift(alternative + newCin);
  });

  return result;
};

export const getLevelAlternativeTaxCodes = (taxCode = "", level = 1) => {
  if (level <= 0 || level > OMOCODIA_MAX_LEVEL)
    console.warn("Invalid omocodia level as argument");
  let alternatives = getAlternativeTaxCodes(taxCode, level);
  return alternatives[level];
};

export const getAlternativeTaxCodes = (
  taxCode = "",
  maxLevel = OMOCODIA_MAX_LEVEL,
  flat = false
) => {
  maxLevel = Math.min(maxLevel, OMOCODIA_MAX_LEVEL);
  let result = {
    1: _getOneLevelAlternativeTaxCodes(taxCode)
  };

  for (let level = 2; level <= maxLevel; level++) {
    result[level] = [];
    let previousLevel = result[level - 1];

    previousLevel.forEach(taxCode => {
      result[level] = result[level].concat(
        _getOneLevelAlternativeTaxCodes(taxCode)
      );
    });
  }

  if (!flat) return result;
  let _result = [];
  for (let level = 1; level <= maxLevel; level++) {
    _result = _result.concat(result[level]);
  }
  return _result;
};

export const getOmocodiaLevel = (taxCode = "") => {
  let partialTaxCode = extractPartialTaxCode(taxCode);
  let digitCounts = partialTaxCode.split("").filter(char => _isDigit(char))
    .length;
  return OMOCODIA_MAX_LEVEL - digitCounts;
};

// CONTROLLI DI VALIDITA'
// ---------------------------------------------------------------------------------------------------------------------
export const getExpectedCin = (taxCode = "") => {
  return generateCin(extractPartialTaxCode(taxCode));
};

export const hasValidLength = (taxCode = "") => {
  return taxCode && taxCode.length === LENGTH;
};

export const hasValidLengthTemporary = (taxCode = "") => {
  return taxCode && taxCode.length === TEMPORARY_LENGTH;
};

export const hasValidCin = (taxCode = "") => {
  return extractCinPart(taxCode) === getExpectedCin(taxCode);
};

export const hasValidForm = (taxCode = "") => {
  return REGEX.test(taxCode);
};
