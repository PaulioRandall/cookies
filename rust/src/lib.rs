//!
//! # A simple key value store
//!
//! Learning Rust by developing a key value store where data can
//! be parsed in 5-15 lines of code in most common programming
//! languages using a very simple regular expression:
//!
//! r"^([_a-zA-Z][_a-zA-Z0-9]+)=([^\n]*)$"
//!

extern crate regex;

pub mod driver;

use std::io;
use driver::Driver;
use driver::MemoryDriver;

type IntResult<T> = Option<Result<T, std::num::ParseIntError>>;
type FloatResult<T> = Option<Result<T, std::num::ParseFloatError>>;
type BoolResult = Option<Result<bool, std::str::ParseBoolError>>;

/// Represents a key value store for accessing and storing key value
/// pairs as well as providing functionality to read and write those
/// pairs to other media.
///
pub struct Store {
    data: driver::Map,
    driver: Box<Driver>
}

/// Implements public store methods.
///
impl Store {

    /// Creates a new store.
    ///
    #[allow(dead_code)]
    pub fn new() -> Store {
        Store {
            data: driver::Map::new(),
            driver: Box::new(MemoryDriver::new())
        }
    }

    /// Creates a new store initialised with some key value pairs.
    ///
    /// * m: Key value pairs to initialise with.
    ///
    #[allow(dead_code)]
    pub fn from(m: driver::Map) -> Store {
        Store {
            data: m,
            driver: Box::new(MemoryDriver::new())
        }
    }

    /// Checks a key is valid.
    ///
    /// * k: Key to check
    ///
    /// Returns: True if key is valid
    ///
    #[allow(dead_code)]
    pub fn check_key(k: &String) -> bool {
        regex::Regex::new(r"^[_a-zA-Z][_a-zA-Z0-9]*$")
            .unwrap()
            .is_match(k)
    }

    /// Checks a value is valid.
    ///
    /// * v: Value to check
    ///
    /// Returns: True if value is valid
    ///
    #[allow(dead_code)]
    pub fn check_value(v: &String) -> bool {
        regex::Regex::new(r"^[^\n]*$")
            .unwrap()
            .is_match(v)
    }

    /// Get the driver.
    ///
    /// Returns: Current driver instance
    ///
    #[allow(dead_code)]
    pub fn driver(&self) -> &Driver {
        &*self.driver
    }

    /// Sets the driver.
    ///
    /// * d: Driver to set
    ///
    #[allow(dead_code)]
    pub fn driver_set(&mut self, d: Box<Driver>) {
        self.driver = d;
    }

    /// Loads key value pairs from the driver clearing all current
    /// entries.
    ///
    /// Returns: Empty result.
    ///
    #[allow(dead_code)]
    pub fn load(&mut self) -> driver::IOCheck {
        self.data = Store::_load(&*self.driver)?;
        Ok(())
    }

    /// Loads key value pairs from a specified driver clearing all
    /// current entries.
    ///
    /// * d: Driver to load from.
    ///
    /// Returns: Empty result.
    ///
    #[allow(dead_code)]
    pub fn load_via_driver(&mut self, d: &Driver) -> driver::IOCheck {
        self.data = Store::_load(d)?;
        Ok(())
    }

    /// Saves key value pairs via the driver.
    ///
    /// Returns: Empty result.
    ///
    #[allow(dead_code)]
    pub fn save(&mut self) -> driver::IOCheck {
        self.driver.save(&self.data)?;
        Ok(())
    }

    /// Saves key value pairs via a specified driver.
    ///
    /// * d: Driver to load from.
    ///
    /// Returns: Empty result.
    ///
    #[allow(dead_code)]
    pub fn save_via_driver(&mut self, d: &mut Driver) -> driver::IOCheck {
        d.save(&self.data)?;
        Ok(())
    }

    /// Gets a value as a string within an option.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get(&self, k: &String) -> Option<&String> {
        self.data.get(k)
    }

    /// Gets a value as a char within an option.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_char(&self, k: &String) -> Option<char> {
        self.data.get(k).map_or(None, |v| v.chars().next())
    }

    /// Gets a value as an i8 within a number result.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_i8(&self, k: &String) -> IntResult<i8> {
        self.data.get(k).map(|v| v.parse::<i8>())
    }

    /// Gets a value as an u8 within a number result.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_u8(&self, k: &String) -> IntResult<u8> {
        self.data.get(k).map(|v| v.parse::<u8>())
    }

    /// Gets a value as an i16 within a number result.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_i16(&self, k: &String) -> IntResult<i16> {
        self.data.get(k).map(|v| v.parse::<i16>())
    }

    /// Gets a value as an u16 within a number result.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_u16(&self, k: &String) -> IntResult<u16> {
        self.data.get(k).map(|v| v.parse::<u16>())
    }

    /// Gets a value as an i32 within a number result.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_i32(&self, k: &String) -> IntResult<i32> {
        self.data.get(k).map(|v| v.parse::<i32>())
    }

    /// Gets a value as an u32 within a number result.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_u32(&self, k: &String) -> IntResult<u32> {
        self.data.get(k).map(|v| v.parse::<u32>())
    }

    /// Gets a value as an i64 within a number result.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_i64(&self, k: &String) -> IntResult<i64> {
        self.data.get(k).map(|v| v.parse::<i64>())
    }

    /// Gets a value as an u64 within a number result.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_u64(&self, k: &String) -> IntResult<u64> {
        self.data.get(k).map(|v| v.parse::<u64>())
    }

    /// Gets a value as an f32 within a number result.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_f32(&self, k: &String) -> FloatResult<f32> {
        self.data.get(k).map(|v| v.parse::<f32>())
    }

    /// Gets a value as an f64 within a number result.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_f64(&self, k: &String) -> FloatResult<f64> {
        self.data.get(k).map(|v| v.parse::<f64>())
    }

    /// Gets a value as an isize within a number result.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_isize(&self, k: &String) -> IntResult<isize> {
        self.data.get(k).map(|v| v.parse::<isize>())
    }

    /// Gets a value as an usize within a number result.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_usize(&self, k: &String) -> IntResult<usize> {
        self.data.get(k).map(|v| v.parse::<usize>())
    }

    /// Gets a value as an bool within a number result.
    ///
    /// * k: Key of the value.
    ///
    /// Returns: Value as a result.
    ///
    #[allow(dead_code)]
    pub fn get_bool(&self, k: &String) -> BoolResult {
        self.data.get(k).map(|v| v.parse::<bool>())
    }
}

/// Implements private store methods.
///
impl Store {

    /// Loads key value pairs from a driver, checks them then
    /// returns them as a map.
    ///
    #[allow(dead_code)]
    fn _load(d: &Driver) -> driver::IOResult {

        let s = d.load()?.clone();

        for (k, v) in s.iter() {

            if !Store::check_key(k) {
                Err::<(), _>(io::Error::new(
                    io::ErrorKind::InvalidData,
                    format!("Invalid key '{}'", k)))?
            }

            if !Store::check_value(v) {
                Err::<(), _>(io::Error::new(
                    io::ErrorKind::InvalidData,
                    format!("Invalid value for '{}'", k)))?
            }
        }

        Ok(s)
    }
}

#[macro_export]
macro_rules! assert_store {
    ( $s:expr, $k:expr, $v:ident ) => ({
        let store: &Store = $s;
        match store.get($k) {
            None => panic!("Expected '{:?}' but key '{:?}' not in store", $v, $k),
            Some(x) => {
                if x != $v {
                    panic!("Expected '{:?}' but found '{:?}'", $v, x);
                }
            }
        }
    });
}

//
// ...end of source code!
//
// Beginning of tests...
//

#[cfg(test)]
macro_rules! str_of {
    ( $s:expr ) => ({
        String::from($s)
    });
}

#[cfg(test)]
macro_rules! string_vec {
    ( $( $v:expr ),* ) => ({
        let mut vec = Vec::new();
        $(
            vec.push(str_of!($v));
        )*
        vec
    });
}


#[cfg(test)]
mod tests {

    use Store;
    use driver;

    type MemDriver = driver::MemoryDriver;

    #[test]
    fn check_key_is_true() {

        let v = string_vec![
            "abc", "efg", "abc_", "a_b_c", "_abc",
            "_123", "_", "___", "a123", "abc123efg"];

        for k in v.iter() {
            let r = Store::check_key(k);
            assert!(r, format!("'{}' is a valid key but failed key checking", k));
        }
    }

    #[test]
    fn check_key_is_false() {

        let v = string_vec![
            "1abc", "-efg", " abc", "abc ", "a bc",
            "123_", "3"];

        for k in v.iter() {
            let r = Store::check_key(k);
            assert!(!r, format!("'{}' is a valid key but failed key checking", k));
        }
    }

    #[test]
    fn check_value_is_true() {

        let v = string_vec![
            "abc", "efsdfsdfsg", "a  bc_   dsf ", "_123", "_abc\r",
            "a123","1a fgdhfk gfdg435 #'a'#sa /*++ */*[a]bc", "-efg", "3"];

        for v in v.iter() {
            let r = Store::check_value(v);
            assert!(r, format!("'{}' is a valid value but failed value checking", v));
        }
    }

    #[test]
    fn check_value_is_false() {

        let v = string_vec![
            "\n", "abc\n", "abc\nefg", "abc efg \n hij",
            "\n\n\n", "\n123", "123\n"];

        for v in v.iter() {
            let r = Store::check_value(v);
            assert!(!r, format!("'{}' is a valid value but failed value checking", v));
        }
    }

    fn make_test_map() -> driver::Map {
        let mut m: driver::Map = driver::Map::new();
        m.insert(str_of!("a"), str_of!("123"));
        m.insert(str_of!("a_neg"), str_of!("-123"));
        m.insert(str_of!("b"), str_of!("4.56"));
        m.insert(str_of!("b_neg"), str_of!("-4.56"));
        m.insert(str_of!("c"), str_of!("789"));
        m.insert(str_of!("c_neg"), str_of!("-789"));
        m.insert(str_of!("d"), str_of!("10.1112"));
        m.insert(str_of!("d_neg"), str_of!("-10.1112"));
        m.insert(str_of!("x"), str_of!("abc"));
        m.insert(str_of!("y"), str_of!("true"));
        m.insert(str_of!("z"), str_of!("demon"));
        m
    }

    fn assert_store(s: &Store) {
        let expected = &str_of!("123");
        assert_store!(s, &str_of!("a"), expected);

        let expected = &str_of!("-123");
        assert_store!(s, &str_of!("a_neg"), expected);

        let expected = &str_of!("4.56");
        assert_store!(s, &str_of!("b"), expected);

        let expected = &str_of!("-4.56");
        assert_store!(s, &str_of!("b_neg"), expected);

        let expected = &str_of!("789");
        assert_store!(s, &str_of!("c"), expected);

        let expected = &str_of!("-789");
        assert_store!(s, &str_of!("c_neg"), expected);

        let expected = &str_of!("10.1112");
        assert_store!(s, &str_of!("d"), expected);

        let expected = &str_of!("-10.1112");
        assert_store!(s, &str_of!("d_neg"), expected);

        let expected = &str_of!("abc");
        assert_store!(s, &str_of!("x"), expected);

        let expected = &str_of!("true");
        assert_store!(s, &str_of!("y"), expected);

        let expected = &str_of!("demon");
        assert_store!(s, &str_of!("z"), expected);

        assert!(s.get(&str_of!("vxgbfhnhfj")).is_none(),
                "Expected NONE but got SOME");
    }

    #[test]
    fn init_load_get() {

        // Create initialised driver
        let m = make_test_map();
        let d = MemDriver::from(m);

        // Create empty store
        let mut s = Store::new();

        // Set driver and load
        s.driver_set(Box::new(d));
        s.load().unwrap();

        // Test store contents
        assert_store(&s);
    }

    #[test]
    fn init_load_via_driver_get() {

        // Create initialised driver
        let m = make_test_map();
        let d= MemDriver::from(m);

        // Create new store
        let mut s = Store::new();

        // Load from driver without setting and test
        s.load_via_driver(&d).unwrap();
        assert_store(&s);
    }

    #[test]
    fn init_save() {

        let m = make_test_map();

        // Create initialised store
        let d = MemDriver::from(m);
        let mut s = Store::new();

        // Set driver and load
        s.driver_set(Box::new(d));
        s.load().unwrap();

        // Set 2nd driver and then save
        let d = MemDriver::new();
        s.driver_set(Box::new(d));
        s.save().unwrap();

        // Load directly from driver
        let m = s.driver.load().unwrap();

        // Create new store with loaded map and test
        let s = Store::from(m);
        assert_store(&s);
    }

    #[test]
    fn init_save_load_get() {

        let m = make_test_map();

        // Create initialised store
        let d = MemDriver::from(m);
        let mut s = Store::new();

        // Set driver and load
        s.driver_set(Box::new(d));
        s.load().unwrap();

        // Save to a 2nd driver without setting
        let mut d = MemDriver::new();
        s.save_via_driver(&mut d).unwrap();

        // Create new empty store and set 2nd driver
        let mut s = Store::new();
        s.driver_set(Box::new(d));

        // Load and test
        s.load().unwrap();
        assert_store(&s);
    }

    fn init_store() -> Store {

        let m = make_test_map();

        // Create initialised store
        let d = MemDriver::from(m);
        let mut s = Store::new();

        // Set driver and load
        s.driver_set(Box::new(d));
        s.load().unwrap();

        s
    }

    #[test]
    fn get_char() {

        let s = init_store();

        let expected: char = 'd'; // d -> demon
        let actual: char = s.get_char(&str_of!("z")).unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_i8() {

        let s = init_store();

        let expected: i8 = 123;
        let actual: i8 = s.get_i8(&str_of!("a")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_neg_i8() {

        let s = init_store();

        let expected: i8 = -123;
        let actual: i8 = s.get_i8(&str_of!("a_neg")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_u8() {

        let s = init_store();

        let expected: u8 = 123;
        let actual: u8 = s.get_u8(&str_of!("a")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_i16() {

        let s = init_store();

        let expected: i16 = 123;
        let actual: i16 = s.get_i16(&str_of!("a")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_neg_i16() {

        let s = init_store();

        let expected: i16 = -123;
        let actual: i16 = s.get_i16(&str_of!("a_neg")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_u16() {

        let s = init_store();

        let expected: u16 = 123;
        let actual: u16 = s.get_u16(&str_of!("a")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_i32() {

        let s = init_store();

        let expected: i32 = 123;
        let actual: i32 = s.get_i32(&str_of!("a")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_neg_i32() {

        let s = init_store();

        let expected: i32 = -123;
        let actual: i32 = s.get_i32(&str_of!("a_neg")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_u32() {

        let s = init_store();

        let expected: u32 = 123;
        let actual: u32 = s.get_u32(&str_of!("a")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_i64() {

        let s = init_store();

        let expected: i64 = 789;
        let actual: i64 = s.get_i64(&str_of!("c")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_neg_i64() {

        let s = init_store();

        let expected: i64 = -789;
        let actual: i64 = s.get_i64(&str_of!("c_neg")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_u64() {

        let s = init_store();

        let expected: u64 = 789;
        let actual: u64 = s.get_u64(&str_of!("c")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_f32() {

        let s = init_store();

        let expected: f32 = 4.56;
        let actual: f32 = s.get_f32(&str_of!("b")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_neg_f32() {

        let s = init_store();

        let expected: f32 = -4.56;
        let actual: f32 = s.get_f32(&str_of!("b_neg")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_f64() {

        let s = init_store();

        let expected: f64 = 10.1112;
        let actual: f64 = s.get_f64(&str_of!("d")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_neg_f64() {

        let s = init_store();

        let expected: f64 = -10.1112;
        let actual: f64 = s.get_f64(&str_of!("d_neg")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_isize() {

        let s = init_store();

        let expected: isize = 789;
        let actual: isize = s.get_isize(&str_of!("c")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_neg_isize() {

        let s = init_store();

        let expected: isize = -789;
        let actual: isize = s.get_isize(&str_of!("c_neg")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_usize() {

        let s = init_store();

        let expected: usize = 789;
        let actual: usize = s.get_usize(&str_of!("c")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }

    #[test]
    fn get_bool() {

        let s = init_store();

        let expected: bool = true;
        let actual: bool = s.get_bool(&str_of!("y")).unwrap().unwrap();

        assert_eq!(expected, actual, "Expected {} but actual is {}", expected, actual);
    }
}